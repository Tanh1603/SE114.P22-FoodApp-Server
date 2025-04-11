package io.foodapp.server.dtos.Menu;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "unitId is required")
    private Long unitId;
}