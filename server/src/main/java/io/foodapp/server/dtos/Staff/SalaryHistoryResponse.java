package io.foodapp.server.dtos.Staff;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryHistoryResponse {
    private int month;
    private int year;
    private BigDecimal currentSalary;
}
