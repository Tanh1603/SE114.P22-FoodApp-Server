package io.foodapp.server.services.Inventory;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.InventoryFilter;
import io.foodapp.server.dtos.Inventory.InventoryResponse;
import io.foodapp.server.dtos.Specification.InventorySpecification;
import io.foodapp.server.mappers.Inventory.InventoryMapper;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.InventoryModel.Inventory;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.repositories.Inventory.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    // ------------------ Get All ------------------
    public Page<InventoryResponse> getInventories(InventoryFilter inventoryFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<Inventory> specification = InventorySpecification.withFilter(inventoryFilter);
            Page<Inventory> inventories = inventoryRepository.findAll(specification, pageable);
            return inventories.map(inventoryMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching inventories: " + e.getMessage());
            throw new RuntimeException("Error fetching inventories", e);
        }
    }

    // ------------------ Nhập hàng ------------------

    public void addToInventoryFromDetail(ImportDetail detail) {
        Ingredient ingredient = detail.getIngredient();

        Optional<Inventory> existingInventoryOpt = inventoryRepository
                .findByIngredientAndExpiryDateAndProductionDateAndCost(
                        ingredient,
                        detail.getExpiryDate().toLocalDate(),
                        detail.getProductionDate().toLocalDate(),
                        detail.getCost());

        if (existingInventoryOpt.isPresent()) {
            Inventory inventory = existingInventoryOpt.get();
            inventory.setQuantityRemaining(inventory.getQuantityRemaining().add(detail.getQuantity()));
            inventory.setOutOfStock(false);
            inventoryRepository.save(inventory);
        } else {
            Inventory inventory = new Inventory();
            inventory.setIngredient(ingredient);
            inventory.setCost(detail.getCost());
            inventory.setExpiryDate(detail.getExpiryDate().toLocalDate());
            inventory.setProductionDate(detail.getProductionDate().toLocalDate());
            inventory.setQuantityRemaining(detail.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void revertInventoryFromDetail(ImportDetail detail) {
        Optional<Inventory> inventoryOpt = inventoryRepository
                .findByIngredientAndExpiryDateAndProductionDateAndCost(
                        detail.getIngredient(),
                        detail.getExpiryDate().toLocalDate(),
                        detail.getProductionDate().toLocalDate(),
                        detail.getCost());

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            BigDecimal quantity = inventory.getQuantityRemaining().subtract(detail.getQuantity());

            if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("Không thể xoá vì đã sử dụng vượt quá số lượng nhập.");
            } else if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                inventory.setOutOfStock(true);
            }

            inventory.setQuantityRemaining(quantity);
            inventoryRepository.save(inventory);
        } else {
            throw new IllegalStateException("Không tìm thấy Inventory để revert.");
        }
    }

    // // ------------------ Bếp yêu cầu nguyên liệu ------------------
    public void exportFromInventory(Long inventoryId, BigDecimal quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tồn kho ID: " + inventoryId));

        BigDecimal remaining = inventory.getQuantityRemaining();

        if (remaining.compareTo(quantity) < 0) {
            throw new IllegalStateException("Tồn kho không đủ!");
        }

        inventory.setQuantityRemaining(remaining.subtract(quantity));
        inventoryRepository.save(inventory);
    }

    public void returnInventory(Long inventoryId, BigDecimal quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tồn kho ID: " + inventoryId));
        BigDecimal remaining = inventory.getQuantityRemaining();
        inventory.setQuantityRemaining(remaining.add(quantity));
        inventoryRepository.save(inventory);
    }
}
