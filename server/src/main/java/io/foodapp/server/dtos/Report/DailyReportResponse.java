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
public class DailyReportResponse {
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDate reportDate;

    private BigDecimal totalSales;
    private BigDecimal totalImportCost;
}
