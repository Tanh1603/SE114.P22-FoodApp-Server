package io.foodapp.server.repositories.Menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findByMenu_Id(Integer menuId, Pageable pageable);
}
