package io.foodapp.server.dtos.Menu;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponse {
    private Long id;
    private Long menuItemId;
    private String name;
    private boolean isDeleted;

    private List<RecipeDetailResponse> recipeDetails;
}
