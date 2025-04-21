package io.foodapp.server.repositories.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.InventoryModel.Export;

@Repository
public interface ExportRepository extends JpaRepository<Export, Long>, JpaSpecificationExecutor<Export>{

}