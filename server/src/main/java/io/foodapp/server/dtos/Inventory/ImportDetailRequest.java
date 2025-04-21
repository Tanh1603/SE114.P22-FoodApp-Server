package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "ImportDetail datetime is required")
    private LocalDateTime expiryDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "ImportDetail datetime is required")
    private LocalDateTime productionDate;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    @NotNull(message = "Cost is required")
    private BigDecimal cost;
}
