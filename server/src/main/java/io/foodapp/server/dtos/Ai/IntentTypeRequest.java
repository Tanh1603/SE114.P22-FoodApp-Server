package io.foodapp.server.dtos.Ai;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntentTypeRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
