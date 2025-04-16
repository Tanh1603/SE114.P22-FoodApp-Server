package io.foodapp.server.dtos.Menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "unitId is required")
    private Long unitId;
}