package io.foodapp.server.repositories.Staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.StaffModel.SalaryHistory;
import io.foodapp.server.models.StaffModel.Staff;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long> {
    // Custom query methods can be defined here if needed
    boolean existsByStaffAndMonthAndYearAndIsDeletedFalse(Staff staff, int month, int year);
}
