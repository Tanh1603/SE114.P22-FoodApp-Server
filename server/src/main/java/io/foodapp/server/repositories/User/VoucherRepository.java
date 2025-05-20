package io.foodapp.server.repositories.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.User.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("""
                SELECT v FROM Voucher v
                WHERE (v.startDate IS NULL OR v.startDate <= CURRENT_DATE)
                  AND (v.endDate IS NULL OR v.endDate >= CURRENT_DATE)
                  AND v.id NOT IN (
                    SELECT cv.voucher.id FROM CustomerVoucher cv
                    WHERE cv.customerId = :customerId AND cv.usedAt IS NOT NULL
                  )
            """)
    List<Voucher> findAvailableVouchersForCustomer(@Param("customerId") String customerId);

}
