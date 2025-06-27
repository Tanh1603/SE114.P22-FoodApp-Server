package io.foodapp.server.controllers.Order;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Order.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.foodapp.server.dtos.Order.OrderStatusRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @ModelAttribute OrderFilter orderFilter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OrderResponse> orders = orderService.getOrders(orderFilter, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(orders));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusRequest newStatus) {

        orderService.updateOrderStatus(id, newStatus);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/check-out")
    public ResponseEntity<Void> checkoutOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Long voucherId = request.get("voucherId") != null ? Long.parseLong(request.get("voucherId")) : null;
        orderService.checkOutOrder(id, voucherId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

}
