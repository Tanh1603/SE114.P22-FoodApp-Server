package io.foodapp.server.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.foodapp.server.services.Report.ReportGeneratorService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportScheduler {

    private final ReportGeneratorService reportGeneratorService;

    // Chạy hàng ngày lúc 00:5 sáng để tạo DailyReport cho ngày hôm trước
    //@Scheduled(cron = "0 5 0 * * *")
    @Scheduled(cron = "0 45 21 28 5 *")
    public void generateDailyReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("Generating daily report for {}", yesterday);
        reportGeneratorService.createDailyReport(yesterday);
    }

    // Chạy vào ngày 1 hàng tháng lúc 00:10 sáng để tạo MonthlyReport và MenuReportDetail cho tháng trước
    //@Scheduled(cron = "0 10 0 1 * *")
    @Scheduled(cron = "0 48 21 28 5 *")
    public void generateMonthlyReports() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        log.info("Generating monthly reports for {}", lastMonth);
        reportGeneratorService.createMonthlyReport(lastMonth);
        reportGeneratorService.createMenuReportDetail(lastMonth);

        LocalDate deleteMonth = LocalDate.now().minusMonths(19).withDayOfMonth(1);
        log.info("Deleting reports older than {}", deleteMonth);
        reportGeneratorService.deleteReportsByMonth(deleteMonth);
    }
}
