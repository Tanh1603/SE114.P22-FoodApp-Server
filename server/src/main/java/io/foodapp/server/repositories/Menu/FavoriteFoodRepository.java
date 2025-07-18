package io.foodapp.server.repositories.Menu;

import io.foodapp.server.models.MenuModel.FavoriteFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
    Optional<FavoriteFood> findByCustomerIdAndFood_Id(String customerId, Long foodId);

    Page<FavoriteFood> findByCustomerId(String customerId, Pageable pageable);
}
