package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.CustomerVoucher;

@Repository
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher, Long> {
    // Custom query methods can be defined here if needed
    
}
