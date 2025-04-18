package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    
    private LocalDateTime expiryDate;

    private LocalDateTime productionDate;

    private BigDecimal quantity;

    private BigDecimal cost;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}