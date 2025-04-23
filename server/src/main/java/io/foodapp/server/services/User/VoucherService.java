package io.foodapp.server.services.User;

import io.foodapp.server.dtos.User.VoucherRequest;
import io.foodapp.server.dtos.User.VoucherResponse;
import io.foodapp.server.mappers.User.VoucherMapper;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.repositories.User.CustomerVoucherRepository;
import io.foodapp.server.repositories.User.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final CustomerVoucherRepository customerVoucherRepository;

    private boolean hasCustomerVoucher(Long id) {
        return customerVoucherRepository.findById(id).isPresent();
    }

    public Page<VoucherResponse> getVouchers(Pageable pageable) {
        try {
            Page<Voucher> vouchers = voucherRepository.findAll(pageable);
            return vouchers.map(voucher -> {
                var res = voucherMapper.toDTO(voucher);
                if(voucher.getEndDate() != null && voucher.getEndDate().isBefore(LocalDate.now())) {
                    res.setExpired(true);
                }
                return res;
            });
        }
        catch (Exception e) {
            throw new RuntimeException("Error getting vouchers: " + e.getMessage());
        }
    }

    public VoucherResponse createVoucher(VoucherRequest request) {
        try {
            boolean validateRequest = request.isStartDateBeforeEndDate();
            if(!validateRequest) {
                throw new RuntimeException("Invalid voucher request");
            }
            return voucherMapper.toDTO(voucherRepository.save(voucherMapper.toEntity(request)));
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating voucher: " + e.getMessage());
        }
    }

    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        try {
            if(hasCustomerVoucher(id)) {
                throw new RuntimeException("Voucher has already been used by a customer and cannot be modified");
            }
            Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher not found"));
            voucherMapper.updateEntityFromDTO(request, voucher);
            return voucherMapper.toDTO(voucherRepository.save(voucher));
        }
        catch (Exception e) {
            throw new RuntimeException("Error updating voucher: " + e.getMessage());
        }
    }

    public void deleteVoucher(Long id) {
        try {
            if(hasCustomerVoucher(id)) {
                throw new RuntimeException("Voucher has already been used by a customer and cannot be modified");
            }
            voucherRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new RuntimeException("Error deleting voucher: " + e.getMessage());
        }
    }
}
