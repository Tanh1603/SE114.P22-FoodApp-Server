package io.foodapp.server.services.User;

import io.foodapp.server.dtos.Filter.VoucherFilter;
import io.foodapp.server.dtos.Specification.VoucherSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "VoucherService")
@Transactional
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final CustomerVoucherRepository customerVoucherRepository;

    private boolean hasCustomerVoucher(Long id) {
        return customerVoucherRepository.findById(id).isPresent();
    }

    public Page<VoucherResponse> getVouchers(Pageable pageable, VoucherFilter voucherFilter) {
        try {
            Specification<Voucher> specification = VoucherSpecification.withFilter(voucherFilter);
            Page<Voucher> vouchers = voucherRepository.findAll(specification, pageable);
            return vouchers.map(voucherMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error getting vouchers: " + e.getMessage());
        }
    }

    public VoucherResponse createVoucher(VoucherRequest request) {
        try {
            return voucherMapper.toDTO(voucherRepository.save(voucherMapper.toEntity(request)));
        } catch (Exception e) {
            throw new RuntimeException("Error creating voucher: " + e.getMessage());
        }
    }

    public VoucherResponse updateVoucher(Long id, VoucherRequest request) {
        try {
            if (hasCustomerVoucher(id)) {
                throw new RuntimeException("Voucher has already been used by a customer and cannot be modified");
            }
            Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher not found"));
            voucherMapper.updateEntityFromDTO(request, voucher);
            return voucherMapper.toDTO(voucherRepository.save(voucher));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating voucher: " + e.getMessage());
        }
    }

    public void deleteVoucher(Long id) {
        try {
            if (hasCustomerVoucher(id)) {
                throw new RuntimeException("Voucher has already been used by a customer and cannot be modified");
            }
            voucherRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting voucher: " + e.getMessage());
        }
    }

    public Page<VoucherResponse> getVoucherForCustomer(Pageable pageable, VoucherFilter filter) {
        try {
            LocalDate now = LocalDate.now();
            Page<Voucher> vouchers ;
            if (filter.getCode() == null || filter.getCode().trim().isEmpty()) {
                vouchers = voucherRepository
                        .findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndQuantityGreaterThan(
                                now, now, 0, pageable);
            } else {
                vouchers = voucherRepository
                        .findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndQuantityGreaterThanAndCodeContainingIgnoreCase(
                                now, now, 0, filter.getCode(), pageable);
            }
            return vouchers.map(voucherMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching vouchers: " + e.getMessage());
        }
    }


}
