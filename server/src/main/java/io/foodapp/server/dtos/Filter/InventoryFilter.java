package io.foodapp.server.dtos.Filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryFilter {
    private String ingredientName;
    private Boolean isOutOfStock;
    private Boolean isExpired;
}