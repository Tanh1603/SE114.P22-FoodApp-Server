package io.foodapp.server.repositories.Report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.foodapp.server.models.Report.MonthlyReport;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, LocalDate> {
        List<MonthlyReport> findByReportMonthBetween(LocalDate startDate, LocalDate endDate);
}
