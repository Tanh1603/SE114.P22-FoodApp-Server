package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.CoffeeTable;

@Repository
public interface CoffeeTableRepository extends JpaRepository<CoffeeTable, Long> {
    // Custom query methods can be defined here if needed
    
}
