package io.foodapp.server.repositories.Menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.MenuItem;

@Repository
public interface MenuItemRepository  extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByIsAvailableFalse();
    List<MenuItem> findByIsAvailableTrue();
}
