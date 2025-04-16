package io.foodapp.server.repositories.Menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Recipe;

@Repository
public interface RecipeRepository  extends JpaRepository<Recipe, Long> {
    List<Recipe> findByIsDeletedFalse();
    List<Recipe> findByIsDeletedTrue();
}
