package io.foodapp.server.repositories.Inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.InventoryModel.Inventory;
import io.foodapp.server.models.MenuModel.Ingredient;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByIngredientAndExpiryDateAndProductionDateAndCost(
        Ingredient ingredient,
        LocalDate expiryDate,
        LocalDate productionDate,
        BigDecimal cost
    );

    List<Inventory> findByIngredientOrderByExpiryDateAsc(Ingredient ingredient);
}
