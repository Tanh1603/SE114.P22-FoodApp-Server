package io.foodapp.server.dtos.Menu;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDetailResponse {
    private Integer id;
    private String menuName;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isAvailable;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    private List<RecipeDetailResponse> recipeDetails;
}
