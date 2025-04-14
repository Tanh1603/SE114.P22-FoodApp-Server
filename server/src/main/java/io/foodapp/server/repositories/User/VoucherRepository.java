package io.foodapp.server.repositories.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.User.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    // Custom query methods can be defined here if needed
    
}
