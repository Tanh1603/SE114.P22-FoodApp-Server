package io.foodapp.server.controllers.Report;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Report.DailyReportResponse;
import io.foodapp.server.dtos.Report.MenuReportDetailResponse;
import io.foodapp.server.dtos.Report.MonthlyReportResponse;
import io.foodapp.server.services.Report.ReportGeneratorService;
import io.foodapp.server.services.Report.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportGeneratorService reportGeneratorService;

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyReportResponse>> getMonthlyReports(@RequestParam int fromYear, int fromMonth, int toYear, int toMonth) {
        return ResponseEntity.ok(reportService.getMonthlyReport(fromYear, fromMonth, toYear, toMonth));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyReportResponse>> getDailyReports(@RequestParam int year, int month) {
        return ResponseEntity.ok(reportService.getDailyReport(year, month));
    }

    @GetMapping("/menu-details")
    public ResponseEntity<List<MenuReportDetailResponse>> getMenuReportDetails(@RequestParam int year, int month) {
        return ResponseEntity.ok(reportService.getMenuReportDetails(year, month));
    }
    
    @GetMapping("/generate")
    public ResponseEntity<Void> generateReports() {
        reportGeneratorService.create();
        return ResponseEntity.ok().build();
    }
}
