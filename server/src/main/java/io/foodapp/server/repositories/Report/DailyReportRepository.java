package io.foodapp.server.repositories.Report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.foodapp.server.models.Report.DailyReport;

public interface DailyReportRepository extends JpaRepository<DailyReport, LocalDate> {
    List<DailyReport> findByReportDateBetween(LocalDate startDate, LocalDate endDate);
}
