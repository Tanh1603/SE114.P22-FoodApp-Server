package io.foodapp.server.repositories.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.ServingType;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByCustomerId(String customerId, Pageable pageable);
    // Custom query methods can be defined here if needed

    List<Order> findAllByCustomerId(String customerId);
    
    List<Order> findByStatusAndStartedAtBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);

    Optional<Order> findByTable_IdAndStatusAndType(Long tableId, OrderStatus status, ServingType type);
}