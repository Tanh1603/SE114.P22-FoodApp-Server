package io.foodapp.server.repositories.Menu;

import io.foodapp.server.models.MenuModel.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
}
