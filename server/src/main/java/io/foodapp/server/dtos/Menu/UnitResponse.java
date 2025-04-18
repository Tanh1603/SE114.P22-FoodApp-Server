package io.foodapp.server.dtos.Menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitResponse {
    private Long id;
    private String name;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
