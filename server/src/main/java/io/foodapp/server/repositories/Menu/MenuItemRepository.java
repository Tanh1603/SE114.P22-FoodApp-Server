package io.foodapp.server.repositories.Menu;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.MenuItem;

@Repository
public interface MenuItemRepository  extends JpaRepository<MenuItem, Long>, JpaSpecificationExecutor<MenuItem> {

}
