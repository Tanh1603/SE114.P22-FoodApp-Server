package io.foodapp.server.dtos.Staff;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryHistoryRequest {
    private Long staffId;
    private int month;
    private int year;
    private double currentSalary;
}
