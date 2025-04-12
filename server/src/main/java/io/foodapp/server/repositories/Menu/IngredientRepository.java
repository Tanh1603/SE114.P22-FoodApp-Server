package io.foodapp.server.repositories.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByIsDeletedFalse();
    List<Ingredient> findByIsDeletedTrue();

    Optional<Ingredient> findByNameAndUnit_Id(String name, Long unitId);
}
