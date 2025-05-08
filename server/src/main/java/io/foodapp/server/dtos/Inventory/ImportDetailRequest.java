package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @NotNull(message = "IngredientId is required")
    private Long ingredientId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "ImportDetail datetime is required")
    private LocalDate expiryDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "ImportDetail datetime is required")
    private LocalDate productionDate;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    @NotNull(message = "Cost is required")
    private BigDecimal cost;
}
