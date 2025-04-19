package io.foodapp.server.repositories.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.FoodTable;

@Repository
public interface FoodTableRepository extends JpaRepository<FoodTable, Long> {
    // Custom query methods can be defined here if needed
    List<FoodTable> findByIsDeletedFalse(); // Example: Find all active coffee tables
    List<FoodTable> findByIsDeletedTrue(); // Example: Find all inactive coffee tables
}
