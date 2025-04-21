package io.foodapp.server.repositories.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.InventoryModel.ExportDetail;

@Repository
public interface ExportDetailRepository extends JpaRepository<ExportDetail, Long>{

}

