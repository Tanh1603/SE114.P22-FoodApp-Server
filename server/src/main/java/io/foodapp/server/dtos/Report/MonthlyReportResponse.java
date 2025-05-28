package io.foodapp.server.dtos.Report;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportResponse {
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDate reportMonth;
    
    private BigDecimal totalSales;
    private BigDecimal totalImportCost;
    private BigDecimal totalSalaries;
    private BigDecimal netProfit;
}
