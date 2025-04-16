package io.foodapp.server.dtos.Menu;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailResponse {
    private Long id;

    private IngredientResponse ingredient;
    
    private double quantity;
    
    private boolean isDeleted;
}
