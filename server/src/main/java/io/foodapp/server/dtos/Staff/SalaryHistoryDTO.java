package io.foodapp.server.dtos.Staff;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryHistoryDTO {

    @NotBlank(message = "ID is required")
    private Long id;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Month is required")
    private int month;

    @NotNull(message = "Year is required")
    private int year;

    @NotBlank(message = "Current salary is required")
    private BigDecimal currentSalary;

    @JsonProperty("isDeleted")
    private boolean deleted;
}
