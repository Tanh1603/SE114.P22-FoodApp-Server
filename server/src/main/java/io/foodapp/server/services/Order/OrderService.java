package io.foodapp.server.services.Order;


import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.dtos.Specification.OrderSpecification;
import io.foodapp.server.mappers.Order.OrderMapper;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.models.User.CustomerVoucher;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import io.foodapp.server.repositories.User.CustomerVoucherRepository;
import io.foodapp.server.repositories.User.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final FoodTableRepository foodTableRepository;
    private final VoucherRepository voucherRepository;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final OrderMapper orderMapper;

    public Page<OrderResponse> getOrdersByUserId(String userId, Pageable pageable) {
        try {
            Page<Order> res = orderRepository.findByCreatedBy(userId, pageable);
            return res.map(orderMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching orders: " + e.getMessage());
            throw new RuntimeException("Error fetching orders", e);
        }
    }

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
            Order order = orderMapper.toEntity(request);
            if (request.getFoodTableId() != null) {
                order.setTable(foodTableRepository.findById(request.getFoodTableId()).orElseThrow(() -> new RuntimeException("Food table not found")));
            } else {
                order.setTable(null);
            }

            if (request.getVoucherId() != null) {
                Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(() -> new RuntimeException("Voucher not found"));
                if (!voucher.isPublished()) {
                    if (request.getCustomerId() != null) {
                        boolean existCustomerVoucher = customerVoucherRepository.existsByCustomerIdAndVoucher_Id(request.getCustomerId(), voucher.getId());
                        if (existCustomerVoucher) {
                            throw new RuntimeException("You have already used this voucher");
                        } else {
                            CustomerVoucher customerVoucher = CustomerVoucher.builder()
                                    .voucher(voucher)
                                    .customerId(request.getCustomerId())
                                    .usedAt(LocalDateTime.now())
                                    .build();
                            customerVoucherRepository.save(customerVoucher);
                        }
                    }
                } else {
                    if(voucher.getTotal() <= 0) throw new RuntimeException("Voucher is no longer available");
                    voucher.setTotal(voucher.getTotal() - 1);
                    voucherRepository.save(voucher);
                }
                order.setVoucher(voucher);
            } else {
                order.setVoucher(null);
            }

            List<Long> menuItemIds = request.getOrderItems().stream().map(OrderItemRequest::getMenuItemId).toList();
            Map<Long, Food> menuItems = foodRepository
                    .findAllById(menuItemIds).stream()
                    .collect(Collectors.toMap(Food::getId, item -> item));
            List<OrderItem> orderItems = request.getOrderItems().stream()
                    .map(item -> {
                        Food food = menuItems.get(item.getMenuItemId());
                        return OrderItem.builder()
                                .food(food)
                                .quantity(item.getQuantity())
                                .order(order)
                                .price(food.getPrice())
                                .foodName(food.getName())
                                .build();
                    }).toList();
            order.setOrderItems(orderItems);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(status);
            orderRepository.saveAndFlush(order);
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
            throw new RuntimeException("Error updating order status", e);
        }
    }

}
