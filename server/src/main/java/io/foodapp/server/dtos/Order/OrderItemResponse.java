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
public class OrderItemResponse {

    private Long id;
    private String menuItemName;
    private double currentPrice;
    private int quantity;

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
