package io.foodapp.server.schedulers;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.foodapp.server.services.Report.ReportGeneratorService;
import io.foodapp.server.services.Staff.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppScheduler {

    private final ReportGeneratorService reportGeneratorService;
    private final StaffService staffService;

    // Chạy hàng ngày lúc 00:5 sáng để tạo DailyReport cho ngày hôm trước
    @Scheduled(cron = "0 5 0 * * *")
    public void generateDailyReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("Generating daily report for {}", yesterday);
        reportGeneratorService.createDailyReport(yesterday);
    }

    // Chạy vào ngày 1 hàng tháng lúc 00:10 sáng
    @Scheduled(cron = "0 10 0 1 * *")
    public void runMonthlyScheduler() {
        staffService.calculateSalary();
        log.info("Salary calculation completed for all staff");

        LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        log.info("Generating monthly reports for {}", lastMonth);
        reportGeneratorService.createMonthlyReport(lastMonth);
        reportGeneratorService.createMenuReportDetail(lastMonth);

        LocalDate deleteMonth = LocalDate.now().minusMonths(19).withDayOfMonth(1);
        log.info("Deleting reports older than {}", deleteMonth);
        reportGeneratorService.deleteReportsByMonth(deleteMonth);
    }
}
