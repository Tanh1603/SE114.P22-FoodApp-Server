package io.foodapp.server.dtos.Menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailRequest {
    private Long id;
    @NotNull(message = "IngredientId is required")
    private Long ingredientId;

    @NotNull(message = "Quantity is required")
    private double quantity;
    
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
