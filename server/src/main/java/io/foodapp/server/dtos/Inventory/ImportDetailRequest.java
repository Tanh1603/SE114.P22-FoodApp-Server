package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportDetailRequest {
    private Long id;

    @NotNull
    private Long ingredientId;

    @NotBlank
    @NotNull
    private LocalDateTime expiryDate;

    @NotBlank
    @NotNull
    private LocalDateTime productionDate;

    @NotBlank
    @NotNull
    private BigDecimal quantity;

    @NotBlank
    @NotNull
    private BigDecimal cost;
}
