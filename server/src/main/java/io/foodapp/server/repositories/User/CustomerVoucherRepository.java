package io.foodapp.server.repositories.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.User.CustomerVoucher;

import java.util.Optional;

@Repository
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher, Long> {
    Optional<CustomerVoucher> findByVoucher_IdAndCustomerId(Long voucherId, String customerId);

    Page<CustomerVoucher> findByCustomerId(String customerId, Pageable pageable);
}
