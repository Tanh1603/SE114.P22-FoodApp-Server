package io.foodapp.server.services.Report;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Report.DailyReportResponse;
import io.foodapp.server.dtos.Report.MenuReportDetailResponse;
import io.foodapp.server.dtos.Report.MonthlyReportResponse;
import io.foodapp.server.mappers.Report.DailyReportMapper;
import io.foodapp.server.mappers.Report.MenuReportDetailMapper;
import io.foodapp.server.mappers.Report.MonthlyReportMapper;
import io.foodapp.server.models.Report.DailyReport;
import io.foodapp.server.models.Report.MenuReportDetail;
import io.foodapp.server.models.Report.MonthlyReport;
import io.foodapp.server.repositories.Report.DailyReportRepository;
import io.foodapp.server.repositories.Report.MenuReportDetailRepository;
import io.foodapp.server.repositories.Report.MonthlyReportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final MonthlyReportRepository monthlyReportRepository;
    private final DailyReportRepository dailyReportRepository;
    private final MenuReportDetailRepository menuReportDetailRepository;

    private final MonthlyReportMapper monthlyReportMapper;
    private final DailyReportMapper dailyReportMapper;
    private final MenuReportDetailMapper menuReportDetailMapper;

    public List<MonthlyReportResponse> getMonthlyReport(int fromYear, int fromMonth, int toYear, int toMonth) {
        try {
            LocalDate start = LocalDate.of(fromYear, fromMonth, 1);
            LocalDate end = LocalDate.of(toYear, toMonth, 1);

            if (start.isBefore(LocalDate.now().minusMonths(19))) {
                throw new RuntimeException("Only save reports for the last 18 months");
            }
            
            List<MonthlyReport> reports = monthlyReportRepository.findByReportMonthBetween(start, end);

            return monthlyReportMapper.toDTOs(reports);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get monthly report", e);
        }
    }

    public List<DailyReportResponse> getDailyReport(int year, int month) {
        try {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            LocalDate limitedMonth = LocalDate.now().minusMonths(19).withDayOfMonth(1);
            if (start.isBefore(limitedMonth)) {
                throw new RuntimeException("Only save reports for the last 18 months");
            }

            List<DailyReport> reports = dailyReportRepository.findByReportDateBetween(start, end);
            return dailyReportMapper.toDTOs(reports);
        } catch (Exception e) {
            throw new RuntimeException("Failed: " + e.getMessage(), e);
        }
    }


    public List<MenuReportDetailResponse> getMenuReportDetails(int year, int month) {
    try {
        LocalDate start = LocalDate.of(year, month, 1);

        LocalDate limitedMonth = LocalDate.now().minusMonths(19).withDayOfMonth(1);
        if (start.isBefore(limitedMonth)) {
            throw new RuntimeException("Only save reports for the last 18 months");
        }

        List<MenuReportDetail> reports = menuReportDetailRepository.findByReportMonth(start);

        // Tính tổng purchaseCount của tất cả menu
        int totalPurchase = reports.stream()
                                   .mapToInt(MenuReportDetail::getPurchaseCount)
                                   .sum();

        // Map sang DTO và tính percentage
        List<MenuReportDetailResponse> responses = reports.stream()
                .map(report -> {
                    float percentage = totalPurchase == 0 ? 0 :
                        (report.getPurchaseCount() * 100.0f) / totalPurchase;

                    return new MenuReportDetailResponse(
                        report.getId(),
                        report.getReportMonth(),
                        report.getMenu().getName(), // Giả sử có phương thức getName() trong Menu
                        report.getPurchaseCount(),
                        percentage
                    );
                })
                .collect(Collectors.toList());

            return responses;
        } catch (Exception e) {
            throw new RuntimeException("Failed: " + e.getMessage(), e);
        }
    }

}
