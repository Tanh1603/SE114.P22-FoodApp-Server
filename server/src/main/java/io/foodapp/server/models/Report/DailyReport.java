package io.foodapp.server.models.Report;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "daily_reports")
public class DailyReport {
    @Id
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate reportDate;

    private BigDecimal totalSales;
    private Integer totalOrders;
}
