package io.foodapp.server.dtos.Menu;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemRequest {
    private Long menuId;

    @NotBlank(message = "Menu item name is required")
    private String name;
    
    private String description;

    @NotNull(message = "Menu item price is required")
    private double price;
    private String imageUrl;
    private boolean isAvailable;

    @NotNull
    private List<RecipeDetailRequest> recipeDetails;
}
