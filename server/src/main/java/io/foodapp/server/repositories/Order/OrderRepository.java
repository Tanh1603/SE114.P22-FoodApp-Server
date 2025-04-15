package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    // Custom query methods can be defined here if needed
    
}
