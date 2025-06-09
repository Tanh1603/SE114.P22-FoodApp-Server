package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.FoodTable;

@Repository
public interface FoodTableRepository extends JpaRepository<FoodTable, Integer>, JpaSpecificationExecutor<FoodTable> {
    // Custom query methods can be defined here if needed
}
