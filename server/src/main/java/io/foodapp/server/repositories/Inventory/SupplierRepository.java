package io.foodapp.server.repositories.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.InventoryModel.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
    List<Supplier> findByIsDeletedFalse();
    List<Supplier> findByIsDeletedTrue();
}
