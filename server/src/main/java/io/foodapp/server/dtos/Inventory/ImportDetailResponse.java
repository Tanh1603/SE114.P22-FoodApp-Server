package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.foodapp.server.dtos.Menu.IngredientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportDetailResponse {
    private Long id;

    private IngredientResponse ingredient;

    
    private LocalDate expiryDate;

    private LocalDate productionDate;

    private BigDecimal quantity;

    private BigDecimal cost;
}