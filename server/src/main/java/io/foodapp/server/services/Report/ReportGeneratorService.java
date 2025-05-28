package io.foodapp.server.services.Report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.models.Report.DailyReport;
import io.foodapp.server.models.Report.MenuReportDetail;
import io.foodapp.server.models.Report.MonthlyReport;
import io.foodapp.server.models.StaffModel.SalaryHistory;
import io.foodapp.server.models.StaffModel.Staff;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.VoucherType;
import io.foodapp.server.repositories.Inventory.ImportRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import io.foodapp.server.repositories.Report.DailyReportRepository;
import io.foodapp.server.repositories.Report.MenuReportDetailRepository;
import io.foodapp.server.repositories.Report.MonthlyReportRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportGeneratorService {
    private final MonthlyReportRepository monthlyReportRepository;
    private final DailyReportRepository dailyReportRepository;
    private final MenuReportDetailRepository menuReportDetailRepository;
    private final ImportRepository importRepository;
    private final OrderRepository orderRepository;
    private final StaffRepository staffRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void createDailyReport(LocalDate date) {
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal importCost = BigDecimal.ZERO;

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Import> imports = importRepository.findByImportDateBetween(start, end);

        for(Import item : imports) {
            for(ImportDetail detail : item.getImportDetails()) {
                importCost = importCost.add(detail.getQuantity().multiply(detail.getCost()));
            }
        }

        List<Order> orders = orderRepository.findByStatusAndStartedAtBetween(OrderStatus.COMPLETED, start, end);
        for(Order order : orders) {
            if (order.getOrderItems() != null) {
                double disCount = 0;
                double total = order.getOrderItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum();
                var voucher = order.getVoucher();
                if (voucher != null) {
                    if (voucher.getType() == VoucherType.PERCENTAGE) {
                        disCount = Math.min(total * (voucher.getValue() / 100), voucher.getMaxValue());
                    } else {
                        disCount = Math.min(voucher.getValue(), voucher.getMaxValue());
                    }
                }
                total = total - disCount;
                totalSales = totalSales.add(BigDecimal.valueOf(total));
            }
        }

        DailyReport report = DailyReport.builder()
            .reportDate(date)
            .totalSales(totalSales)
            .totalImportCost(importCost)
            .build();
        dailyReportRepository.save(report);
    }

    @Transactional
    public void createMonthlyReport(LocalDate month) {
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalImportCost = BigDecimal.ZERO;
        BigDecimal totalSalaries;
        BigDecimal netProfit;

        LocalDate start = month.withDayOfMonth(1);
        LocalDate end = month.withDayOfMonth(month.lengthOfMonth());

        var dailyReport = dailyReportRepository.findByReportDateBetween(start, end);
        for(DailyReport report : dailyReport) {
            totalSales = totalSales.add(report.getTotalSales());
            totalImportCost = totalImportCost.add(report.getTotalImportCost());
        }

        // cần tính lương
        totalSalaries = calculateTotalSalary(start, end);
        netProfit = (totalSales.add(totalImportCost)).add(totalSalaries);

        MonthlyReport report = MonthlyReport.builder()
            .reportMonth(start)
            .totalSales(totalSales)
            .totalImportCost(totalImportCost)
            .totalSalaries(totalSalaries)
            .netProfit(netProfit)
            .build();
        
        monthlyReportRepository.save(report);
    }

    @Transactional
    public void createMenuReportDetail(LocalDate month) {
        LocalDateTime start = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByStatusAndStartedAtBetween(OrderStatus.COMPLETED, start, end);

        // Dùng Map theo menuId thay vì Menu object
        Map<Integer, Integer> salesByMenuId = new HashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                Food food = item.getFood();
                Integer menuId = food.getMenu().getId(); // lấy ID menu
                Integer quantity = item.getQuantity();
                salesByMenuId.merge(menuId, quantity, Integer::sum);
            }
        }

        for (Map.Entry<Integer, Integer> entry : salesByMenuId.entrySet()) {
            Integer menuId = entry.getKey();
            Integer totalQuantity = entry.getValue();

            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("Menu not found: id = " + menuId));

            // Tạo mới SalesReportDetail
            MenuReportDetail report = MenuReportDetail.builder()
                .reportMonth(month.withDayOfMonth(1))
                .menu(menu)
                .totalQuantity(totalQuantity)
                .build();

            menuReportDetailRepository.save(report);
        }
    }

    public void deleteReportsByMonth(LocalDate month) {
        LocalDate start = month.withDayOfMonth(1);
        LocalDate end = month.withDayOfMonth(month.lengthOfMonth());

        var monthlyReport = monthlyReportRepository.findById(start);
        if(monthlyReport.isPresent()) {
            monthlyReportRepository.delete(monthlyReport.get());
        }

        var dailyReport = dailyReportRepository.findByReportDateBetween(start, end);
        if(!dailyReport.isEmpty()) {
            dailyReport.forEach(dailyReportRepository::delete);
        }

        var menuReportDetail = menuReportDetailRepository.findByReportMonth(start);
        if(!menuReportDetail.isEmpty()) {
            menuReportDetail.forEach(menuReportDetailRepository::delete);
        }
    }

    public BigDecimal calculateTotalSalary(LocalDate start, LocalDate end) {
        BigDecimal totalSalaries = BigDecimal.ZERO;

        List<Staff> staffs = staffRepository.findAll().stream()
            .filter(staff ->
                (staff.getStartDate() == null || !staff.getStartDate().isAfter(end)) &&
                (staff.getEndDate() == null || !staff.getEndDate().isBefore(start))
            )
            .toList();

        for (Staff staff : staffs) {
            Optional<SalaryHistory> salary = staff.getSalaryHistories().stream()
                .filter(s -> s.getMonth() == start.getMonth().getValue() && s.getYear() == start.getYear())
                .findFirst();

            BigDecimal salaryToAdd;

            if (salary.isPresent()) {
                salaryToAdd = BigDecimal.valueOf(salary.get().getCurrentSalary());
            } else {

                LocalDate actualStart = staff.getStartDate() != null && staff.getStartDate().isAfter(start)
                    ? staff.getStartDate()
                    : start;

                LocalDate actualEnd = staff.getEndDate() != null && staff.getEndDate().isBefore(end)
                    ? staff.getEndDate()
                    : end;

                long workingDays = ChronoUnit.DAYS.between(actualStart, actualEnd.plusDays(1)); // inclusive
                long totalDays = ChronoUnit.DAYS.between(start, end.plusDays(1)); // full month

                BigDecimal ratio = BigDecimal.valueOf(workingDays).divide(BigDecimal.valueOf(totalDays), 6, RoundingMode.HALF_UP);
                salaryToAdd = BigDecimal.valueOf(staff.getBasicSalary()).multiply(ratio);
            }

            totalSalaries = totalSalaries.add(salaryToAdd);
        }
        return totalSalaries;
    }
}
