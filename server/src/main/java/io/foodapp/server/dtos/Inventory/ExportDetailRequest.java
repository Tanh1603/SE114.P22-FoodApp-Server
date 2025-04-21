package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportDetailRequest {
    private Long id;

    @NotNull(message = "IngredientId is required")
    private Long inventoryId;

    @NotNull(message = "ExportDetail datetime is required")
    private BigDecimal quantity;
}
