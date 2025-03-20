package io.foodapp.server.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequest {

    @NotBlank
    @NonNull
    private String position;

    @NotBlank
    @NonNull
    private LocalDate birthDate;

    @NotBlank
    @NonNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank
    @NonNull
    private BigDecimal basicSalary;

}
