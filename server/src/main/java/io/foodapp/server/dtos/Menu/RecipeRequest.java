package io.foodapp.server.dtos.Menu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequest {

    private Long menuItemId;

    @NotBlank(message = "Recipe name is required")
    private String name;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private List<RecipeDetailRequest> recipeDetails;
}
