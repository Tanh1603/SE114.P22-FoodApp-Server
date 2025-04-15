package io.foodapp.server.dtos.Order;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    private Long id;
    private Long menuItemId;
    private int quantity;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
