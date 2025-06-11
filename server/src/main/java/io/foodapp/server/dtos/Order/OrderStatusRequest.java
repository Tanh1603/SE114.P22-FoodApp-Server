package io.foodapp.server.dtos.Order;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.foodapp.server.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusRequest {
    private OrderStatus status;
    private String sellerId;
    private String customerId;
    private String shipperId;
}
