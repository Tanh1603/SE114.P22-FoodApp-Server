package io.foodapp.server.repositories.Menu;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {

    Page<Food> findByMenu_Id(Integer menuId, Specification<Food> filter, Pageable pageable);

    List<Food> findTop10ByOrderByTotalLikesDesc();
}
