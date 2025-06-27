package io.foodapp.server.services.Order;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.dtos.Specification.OrderSpecification;
import io.foodapp.server.mappers.Order.OrderItemMapper;
import io.foodapp.server.mappers.Order.OrderMapper;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.models.User.CustomerVoucher;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.ServingType;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import io.foodapp.server.repositories.Order.OrderItemRepository;
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

import io.foodapp.server.dtos.Notification.OrderNotification;
import io.foodapp.server.dtos.Order.OrderStatusRequest;
import io.foodapp.server.models.Order.FcmToken;
import io.foodapp.server.models.enums.UserType;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "OrderService")
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final FoodRepository foodRepository;
    private final FoodTableRepository foodTableRepository;
    private final VoucherRepository voucherRepository;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final OrderMapper orderMapper;
    private final FcmTokenService fcmTokenService;
    private final FirebaseNotificationService notificationService;
    private final String sellerId = SecurityUtils.getCurrentCustomerId();

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
            Order order = orderMapper.toEntity(request);
            if (request.getFoodTableId() != null) {
                order.setTable(foodTableRepository.findById(request.getFoodTableId())
                        .orElseThrow(() -> new RuntimeException("Food table not found")));
            } else {
                order.setTable(null);
            }

            if (request.getType().equals(ServingType.ONLINE.name()) && request.getCustomerId() == null) {
                throw new RuntimeException("CustomerId not null for serving type online");
            } else if ((request.getType().equals(ServingType.INSTORE.name()) ||
                    request.getType().equals(ServingType.TAKEAWAY.name())) && request.getSellerId() == null) {
                throw new RuntimeException("SellerId not null for serving type TAKEAWAY or INSTORE");
            }

            // handle voucher
            if (request.getVoucherId() != null) {
                Long voucherId = request.getVoucherId();
                LocalDate now = LocalDate.now();
                Voucher voucher = voucherRepository.findById(voucherId)
                        .orElseThrow(() -> new RuntimeException("Voucher not found for id: " + voucherId));
                if (voucher.getQuantity() == 0) {
                    throw new RuntimeException("Voucher is no longer available");
                }

                if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                    throw new RuntimeException("Voucher is no longer available");
                }

                boolean isUsed = customerVoucherRepository
                        .findByVoucher_IdAndCustomerId(voucherId, request.getCustomerId())
                        .isPresent();

                if (isUsed) {
                    throw new RuntimeException("You have already used this voucher");
                }

                customerVoucherRepository.save(CustomerVoucher.builder()
                        .voucher(voucher)
                        .customerId(request
                                .getCustomerId())
                        .usedAt(LocalDateTime.now())
                        .build());

                voucher.setQuantity(voucher.getQuantity() - 1);

                order.setVoucher(voucherRepository.save(voucher));

            } else {
                order.setVoucher(null);
            }

            // handle orderItem
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
                                .foodImages(food.getImages())
                                .build();
                    }).toList();
            order.setOrderItems(orderItems);
            Order newOrder = orderRepository.saveAndFlush(order);
            if (newOrder.getCustomerId() != null) {
                var fcm = fcmTokenService.getFcmTokenByType(UserType.SELLER);

                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng mới #" + newOrder.getId())
                                .body("Có đơn hàng mới cần xác nhận")
                                .token(fcm.getToken())
                                .build());
            }
            return orderMapper.toDTO(newOrder);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatusRequest request) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            FcmToken fcm;
            if (request.getStatus().name().equals(OrderStatus.CANCELLED.name())) {
                if (request.getCustomerId() == null) {
                    throw new RuntimeException("CustomerId not null");
                }

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
                    customerVoucherRepository.deleteByVoucher_IdAndCustomerId(voucher.getId(), request.getCustomerId());
                }

                fcm = fcmTokenService.getFcmTokenByType(UserType.SELLER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Hủy đơn hàng")
                                .body("Đơn hàng #" + order.getId() + "đã bị hủy")
                                .token(fcm.getToken())
                                .build());

                fcm = fcmTokenService.getFcmTokenByType(UserType.SHIPPER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Hủy đơn hàng")
                                .body("Đơn hàng #" + order.getCustomerId() + "đã bị hủy")
                                .token(fcm.getToken())
                                .build());
            }

            if (request.getStatus().name().equals(OrderStatus.CONFIRMED.name())) {
                if (request.getSellerId() == null) {
                    throw new RuntimeException("SellerId not null");
                }
                order.setSellerId(request.getSellerId());

                fcm = fcmTokenService.getFcmToken(order.getCustomerId(), UserType.CUSTOMER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Xác nhận đơn hàng")
                                .body("Đơn hàng #" + order.getId() + "đã được xác nhận xác nhận")
                                .token(fcm.getToken())
                                .build());
            }

            if (request.getStatus().name().equals(OrderStatus.READY.name())) {
                fcm = fcmTokenService.getFcmTokenByType(UserType.SHIPPER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng mới")
                                .body("Đơn hàng #" + order.getId() + "cần được giao")
                                .token(fcm.getToken())
                                .build());
            }

            if (request.getStatus().name().equals(OrderStatus.SHIPPING.name())) {
                if (request.getShipperId() == null) {
                    throw new RuntimeException("ShipperId not null");
                }
                order.setShipperId(request.getShipperId());
                fcm = fcmTokenService.getFcmToken(order.getCustomerId(), UserType.CUSTOMER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng đang giao")
                                .body("Đơn hàng #" + order.getId() + "đang được shipper giao")
                                .token(fcm.getToken())
                                .build());
            }

            if (request.getStatus().name().equals(OrderStatus.COMPLETED.name())) {
                order.setShipperId(request.getShipperId());

                fcm = fcmTokenService.getFcmToken(order.getCustomerId(), UserType.CUSTOMER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng giao thành công")
                                .body("Đơn hàng #" + order.getId() + "đang đã được giao thành công")
                                .token(fcm.getToken())
                                .build());

                fcm = fcmTokenService.getFcmTokenByType(UserType.SELLER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng giao thành công")
                                .body("Đơn hàng #" + order.getId() + "đang đã được giao thành công")
                                .token(fcm.getToken())
                                .build());

                fcm = fcmTokenService.getFcmTokenByType(UserType.SHIPPER);
                notificationService.sendNotification(
                        fcm.getUserId(),
                        OrderNotification.builder()
                                .title("Đơn hàng giao thành công")
                                .body("Đơn hàng #" + order.getId() + "đang đã được giao thành công")
                                .token(fcm.getToken())
                                .build());

                order.setPaymentAt(LocalDateTime.now());
            }

            order.setStatus(request.getStatus());
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (RuntimeException e) {
            log.error("Error updating order status: {}", e.getMessage());
            throw new RuntimeException("Error updating order status" + e.getMessage(), e);
        }
    }

    // Service methods for seller
    public OrderResponse checkOutOrder(Long id, Long voucherId) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            var totalPrice = order.getOrderItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            if (voucherId != null) {
                Voucher voucher = voucherRepository.findById(voucherId)
                        .orElseThrow(() -> new RuntimeException("Voucher not found for id: " + voucherId));

                if (voucher.getQuantity() == 0) {
                    throw new RuntimeException("Voucher is no longer available");
                }
                LocalDate now = LocalDate.now();
                if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                    throw new RuntimeException("Voucher is no longer available");
                }
                if( voucher.getMinOrderPrice() > totalPrice) {
                    throw new RuntimeException("Order price is not enough to use this voucher");
                }
                order.setVoucher(voucher);
                voucher.setQuantity(voucher.getQuantity() - 1);
                voucherRepository.save(voucher);
            }
            order.setPaymentAt(LocalDateTime.now());
            order.setStatus(OrderStatus.COMPLETED);
            order.setSellerId(sellerId);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (RuntimeException e) {
            log.error("Error checking out order: {}", e.getMessage());
            throw new RuntimeException("Error checking out order: " + e.getMessage(), e);
        }
    }

    public OrderResponse cancelOrder(Long id) {
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                Food food = item.getFood();
                food.setRemainingQuantity(food.getRemainingQuantity() + item.getQuantity());
                foodRepository.save(food);
            }
            Voucher voucher = order.getVoucher();
            if (voucher != null) {
                voucher.setQuantity(voucher.getQuantity() + 1);
                voucherRepository.save(voucher);
            }
            order.setVoucher(null);
            order.setSellerId(sellerId);
            order.setStatus(OrderStatus.CANCELLED);
            return orderMapper.toDTO(orderRepository.saveAndFlush(order));
        } catch (RuntimeException e) {
            log.error("Error cancelling order: {}", e.getMessage());
            throw new RuntimeException("Error cancelling order: " + e.getMessage(), e);
        }
    }

    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        try {
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found for id: " + request.getOrderId()));
            Food food = foodRepository.findById(request.getFoodId())
                    .orElseThrow(() -> new RuntimeException("Food not found for id: " + request.getFoodId()));
            if (food.getRemainingQuantity() < request.getQuantity()) {
                throw new RuntimeException("Food quantity is not enough");
            }
            food.setRemainingQuantity(food.getRemainingQuantity() - request.getQuantity());
            foodRepository.save(food);
            OrderItem orderItem = OrderItem.builder()
                    .food(food)
                    .quantity(request.getQuantity())
                    .order(order)
                    .price(food.getPrice())
                    .foodName(food.getName())
                    .foodImages(food.getImages())
                    .build();

            return orderItemMapper.toDTO(orderItemRepository.save(orderItem));
        } catch (RuntimeException e) {
            log.error("Error creating order item: {}", e.getMessage());
            throw new RuntimeException("Error creating order item: " + e.getMessage(), e);
        }
    }

    public OrderItemResponse updateOrderItemQuantity(Long id, int quantity) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order item not found for id: " + id));
            Food food = orderItem.getFood();
            int oldQuantity = food.getRemainingQuantity() + orderItem.getQuantity();
            if (oldQuantity < quantity) {
                throw new RuntimeException("Food quantity is not enough");
            }
            food.setRemainingQuantity(oldQuantity - quantity);
            foodRepository.save(food);
            orderItem.setFood(food);
            orderItem.setQuantity(quantity);
            orderRepository.save(orderItem.getOrder());
            return orderItemMapper.toDTO(orderItem);
        } catch (RuntimeException e) {
            log.error("Error updating order item: {}", e.getMessage());
            throw new RuntimeException("Error updating order item: " + e.getMessage(), e);
        }
    }

    public void deleteOrderItem(Long id) {
        try {
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order item not found for id: " + id));
            Food food = orderItem.getFood();
            food.setRemainingQuantity(food.getRemainingQuantity() + orderItem.getQuantity());
            foodRepository.save(food);
            orderItemRepository.delete(orderItem);
        } catch (RuntimeException e) {
            log.error("Error deleting order item: {}", e.getMessage());
            throw new RuntimeException("Error deleting order item: " + e.getMessage(), e);
        }
    }
}
