package io.foodapp.server.controllers.Report;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Report.DailyReportResponse;
import io.foodapp.server.dtos.Report.MenuReportDetailResponse;
import io.foodapp.server.dtos.Report.MonthlyReportResponse;
import io.foodapp.server.services.Report.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/monthly")
    public List<MonthlyReportResponse> getMonthlyReports() {
        return reportService.getMonthlyReport();
    }

    @GetMapping("/daily")
    public List<DailyReportResponse> getDailyReports(@RequestParam int year, int month) {
        return reportService.getDailyReport(year, month);
    }

    @GetMapping("/menu-details")
    public List<MenuReportDetailResponse> getMenuReportDetails(@RequestParam int year, int month) {
        return reportService.getMenuReportDetails(year, month);
    } 
}
