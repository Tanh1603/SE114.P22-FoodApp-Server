package io.foodapp.server.repositories.Menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    
}
