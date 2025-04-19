package io.foodapp.server.services.Order;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.dtos.Specification.OrderSpecification;
import io.foodapp.server.mappers.Order.OrderItemMapper;
import io.foodapp.server.mappers.Order.OrderMapper;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.repositories.Menu.MenuItemRepository;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import io.foodapp.server.repositories.Order.OrderItemRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import io.foodapp.server.repositories.User.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final StaffRepository staffRepository;
    private final FoodTableRepository foodTableRepository;
    private final VoucherRepository voucherRepository;

    // private final OrderHelper orderHelper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public Page<OrderResponse> getOrders(OrderFilter orderFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<Order> specification = OrderSpecification.withFilter(orderFilter);
            Page<Order> orders = orderRepository.findAll(specification, pageable);
            return orders.map(orderMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching orders: " + e.getMessage());
            throw new RuntimeException("Error fetching orders", e);
        }
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        try {
            Order order = orderMapper.toEntity(
                    request,
                    foodTableRepository,
                    voucherRepository,
                    staffRepository,
                    menuItemRepository,
                    orderItemMapper,
                    orderItemRepository);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            // Cập nhật các trường cơ bản
            orderMapper.updateEntityFromDto(orderRequest, order,
                    foodTableRepository,
                    voucherRepository,
                    staffRepository,
                    menuItemRepository,
                    orderItemMapper,
                    orderItemRepository);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (Exception e) {
            System.out.println("Error updating order: " + e.getLocalizedMessage());
            throw new RuntimeException("Error updating order: " + e.getMessage());
        }
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(status);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
            throw new RuntimeException("Error updating order status", e);
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            order.getOrderItems().forEach(orderItem -> {
                orderItem.setDeleted(true);
                orderItemRepository.save(orderItem);
            });
            order.setDeleted(true);
            orderRepository.save(order);
        } catch (Exception e) {
            System.out.println("Error deleting order: " + e.getMessage());
            throw new RuntimeException("Error deleting order", e);
        }
    }
}
