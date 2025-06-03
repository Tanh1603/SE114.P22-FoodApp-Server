package io.foodapp.server.dtos.Report;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuReportDetailResponse {
    private Integer id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate reportMonth;
    
    private String menuName;
    private Integer purchaseCount;
}
