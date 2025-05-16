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
import io.foodapp.server.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "OrderService")
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final FoodTableRepository foodTableRepository;
    private final VoucherRepository voucherRepository;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final OrderMapper orderMapper;
    private final String customerId = SecurityUtils.getCurrentCustomerId();

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

    public OrderResponse createOrder(OrderRequest request) {
        try {
            log.info("Request: {}", request);
            Order order = orderMapper.toEntity(request);


            if (request.getFoodTableId() != null) {
                order.setTable(foodTableRepository.findById(request.getFoodTableId()).orElseThrow(() -> new RuntimeException("Food table not found")));
            } else {
                order.setTable(null);
            }

            if (request.getVoucherId() != null) {
                Long voucherId = request.getVoucherId();
                LocalDate now = LocalDate.now();
                Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new RuntimeException("Voucher not found for id: " + voucherId));
                if (voucher.getQuantity() == 0) {
                    throw new RuntimeException("Voucher is no longer available");
                }

                if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                    throw new RuntimeException("Voucher is no longer available");
                }

                boolean isUsed = customerVoucherRepository.findByVoucher_IdAndCustomerId(voucherId, customerId).isPresent();

                if (isUsed) {
                    throw new RuntimeException("You have already used this voucher");
                }

                customerVoucherRepository.save(CustomerVoucher.builder()
                        .voucher(voucher)
                        .customerId(customerId)
                        .usedAt(LocalDateTime.now())
                        .build());

                voucher.setQuantity(voucher.getQuantity() - 1);

                order.setVoucher(voucherRepository.save(voucher));

            } else {
                order.setVoucher(null);
            }

            List<Long> foodIds = request.getOrderItems().stream().map(OrderItemRequest::getFoodId).toList();
            Map<Long, Food> foods = foodRepository
                    .findAllById(foodIds).stream()
                    .collect(Collectors.toMap(Food::getId, item -> item));
            List<OrderItem> orderItems = request.getOrderItems().stream()
                    .map(item -> {
                        Food food = foods.get(item.getFoodId());

                        if (food == null) {
                            throw new RuntimeException("Food not found for id: " + item.getFoodId());
                        }

                        if (food.getRemainingQuantity() == 0) {
                            throw new RuntimeException("Food is no longer available");
                        }

                        if (food.getRemainingQuantity() < item.getQuantity()) {
                            throw new RuntimeException("Food quantity is not enough");
                        }

                        food.setRemainingQuantity(food.getRemainingQuantity() - item.getQuantity());
                        foodRepository.save(food);

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

    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

            if (status.name().equals(OrderStatus.CANCELLED.name())) {
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem item : orderItems) {
                    Food food = item.getFood();
                    food.setRemainingQuantity(food.getRemainingQuantity() + item.getQuantity());
                    foodRepository.save(food);
                }

                Voucher voucher = order.getVoucher();
                if (order.getVoucher() != null) {
                    voucher.setQuantity(voucher.getQuantity() + 1);
                    voucherRepository.save(voucher);
                    customerVoucherRepository.deleteByVoucher_IdAndCustomerId(voucher.getId(), customerId);
                }
            }

            order.setStatus(status);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
            throw new RuntimeException("Error updating order status", e);
        }
    }
}
