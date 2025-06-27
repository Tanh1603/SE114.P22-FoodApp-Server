package io.foodapp.server.controllers.Order;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.services.Order.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderService orderService;

    @PostMapping
    public OrderItemResponse createOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        return orderService.createOrderItem(orderItemRequest);
    }

    @PatchMapping("/{id}/quantity")
    public OrderItemResponse updateOrderItemQuantity(@PathVariable Long id, @RequestBody Map<String, String> quantity) {
        int quantityValue = Integer.parseInt(quantity.get("quantity"));
        if (quantityValue <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        return orderService.updateOrderItemQuantity(id, quantityValue);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderItem(@PathVariable Long id) {
        orderService.deleteOrderItem(id);
    }
}
