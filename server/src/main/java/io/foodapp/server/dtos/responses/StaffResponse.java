package io.foodapp.server.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffResponse {

    private Integer id;

    private String position;

    private LocalDate birthDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal basicSalary;

    private boolean isDeleted;
}
