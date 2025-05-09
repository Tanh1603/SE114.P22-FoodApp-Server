package io.foodapp.server.controllers.Order;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.services.Order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{customerId}")
    public ResponseEntity<PageResponse<OrderResponse>> getOrdersByCustomerId(
            @PathVariable String customerId, @ModelAttribute PageFilter filter) {
        return ResponseEntity.ok(PageResponse.fromPage(orderService.getOrdersByUserId(customerId, PageFilter.toPageAble(filter))));
    }

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
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id,
                                                  @RequestBody OrderStatus orderRequest) {
        orderService.updateOrderStatus(id, orderRequest);
        return ResponseEntity.noContent().build();
    }

}
