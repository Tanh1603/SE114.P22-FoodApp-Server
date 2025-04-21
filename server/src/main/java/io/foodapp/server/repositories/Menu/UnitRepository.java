package io.foodapp.server.repositories.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.MenuModel.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findByIsActiveFalse();
    List<Unit> findByIsActiveTrue();
    Optional<Unit> findByName(String name);
}
