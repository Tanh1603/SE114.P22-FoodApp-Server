package io.foodapp.server.repositories.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.User.Voucher;

import java.time.LocalDate;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {

    Page<Voucher> findByStartDateLessThanAndEndDateGreaterThanAndQuantityGreaterThan(LocalDate startDateIsLessThan, LocalDate endDateIsGreaterThan, int quantityIsGreaterThan, Pageable pageable);
}
