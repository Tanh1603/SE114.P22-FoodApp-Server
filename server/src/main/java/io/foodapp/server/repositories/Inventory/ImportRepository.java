package io.foodapp.server.repositories.Inventory;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.InventoryModel.Import;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long>, JpaSpecificationExecutor<Import>{
    List<Import> findByImportDateBetween(LocalDate start, LocalDate end);
}
