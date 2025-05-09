package io.foodapp.server.services.User;

import io.foodapp.server.dtos.User.AddressRequest;
import io.foodapp.server.dtos.User.AddressResponse;
import io.foodapp.server.dtos.User.VoucherResponse;
import io.foodapp.server.mappers.User.AddressMapper;
import io.foodapp.server.mappers.User.VoucherMapper;
import io.foodapp.server.models.User.Address;
import io.foodapp.server.models.User.CustomerVoucher;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.repositories.User.AddressRepository;
import io.foodapp.server.repositories.User.CustomerVoucherRepository;
import io.foodapp.server.repositories.User.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final VoucherRepository voucherRepository;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final VoucherMapper voucherMapper;

    public VoucherResponse receiveVoucher(String customerId, Long voucherId) {
        try {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new RuntimeException("Voucher not found"));
            if (voucher.getTotal() == 0) {
                throw new RuntimeException("Voucher is no longer available");
            }
            boolean alreadyReceived = customerVoucherRepository
                    .findByVoucher_IdAndCustomerId(voucherId, customerId)
                    .isPresent();
            if (alreadyReceived) {
                throw new RuntimeException("You have already received this voucher");
            }
            customerVoucherRepository.save(CustomerVoucher.builder().voucher(voucher).customerId(customerId).build());
            voucher.setTotal(voucher.getTotal() - 1);
            return voucherMapper.toDTO(voucherRepository.save(voucher));
        } catch(RuntimeException e) {
            throw new RuntimeException("Error receiving voucher: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Error receiving voucher: " + e.getMessage());
        }
    }

    public Page<VoucherResponse> getVoucherByUserId(String userId, Pageable pageable) {
        try {
            Page<CustomerVoucher> customerVouchers = customerVoucherRepository.findByCustomerId(userId, pageable);
            return customerVouchers.map(cv -> {
                Voucher voucher = cv.getVoucher();
                var res = voucherMapper.toDTO(voucher);
                res.setUsedAt(cv.getUsedAt());
                if (voucher.getEndDate() != null && voucher.getEndDate().isBefore(LocalDate.now())) {
                    res.setExpired(true);
                }
                return res;
            });
        } catch (Exception e) {
            throw new RuntimeException("Error fetching vouchers: " + e.getMessage());
        }
    }

    // address service
    public List<AddressResponse> getAddressesByUserId(String userId) {
        try {
            return addressMapper.toDTO(addressRepository.findByUserId(userId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching addresses: " + e.getMessage());
        }
    }

    public AddressResponse createAddress(String userId, AddressRequest request) {
        try {
            Address address = addressMapper.toEntity(request);
            address.setUserId(userId);
            return addressMapper.toDTO(addressRepository.save(address));
        } catch (Exception e) {
            throw new RuntimeException("Error creating address: " + e.getMessage());
        }
    }

    public AddressResponse updateAddress(String userId, Long id, AddressRequest request) {
        try {
            var existingAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
            addressMapper.updateEntityFromDTO(request, existingAddress);
            existingAddress.setUserId(userId);
            return addressMapper.toDTO(addressRepository.save(existingAddress));
        } catch (Exception e) {
            throw new RuntimeException("Error updating address: " + e.getMessage());
        }
    }

    public void deleteAddress(String userId, Long id) {
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            if (!address.getUserId().equals(userId)) {
                throw new RuntimeException("You do not have permission to delete this address.");
            }

            addressRepository.delete(address);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting address: " + e.getMessage());
        }
    }

}
