package io.foodapp.server.dtos.Ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntentTypeResponse {
    private Long id;
    private String name;
}
