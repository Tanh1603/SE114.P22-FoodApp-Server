package io.foodapp.server.dtos.Menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {
    private Long id;

    private String name;

    private String unit;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
