package io.foodapp.server.repositories.Staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.foodapp.server.models.StaffModel.SalaryHistory;
import io.foodapp.server.models.StaffModel.Staff;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {
    // Custom query methods can be defined here if needed
    boolean existsByStaffAndMonthAndYear(Staff staff, int month, int year);

    @Query("SELECT SUM(sh.currentSalary) FROM SalaryHistory sh WHERE sh.month = :month AND sh.year = :year")
    Double getTotalSalaryByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
