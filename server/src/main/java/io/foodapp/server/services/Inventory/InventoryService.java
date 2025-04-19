package io.foodapp.server.services.Inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.InventoryModel.Inventory;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.repositories.Inventory.InventoryRepository;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;

    // ------------------ Get All ------------------
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> !inv.isDeleted())
                .toList();
    }

    // ------------------ Nhập hàng ------------------
    public void updateInventoryFromImport(Import anImport) {
        for (ImportDetail detail : anImport.getImportDetails()) {
            updateInventoryFromDetail(detail);
        }
    }

    private void updateInventoryFromDetail(ImportDetail detail) {
        Ingredient ingredient = detail.getIngredient();

        Optional<Inventory> existingInventoryOpt = inventoryRepository
                .findByIngredientAndExpiryDateAndProductionDateAndCost(
                        ingredient,
                        detail.getExpiryDate().toLocalDate(),
                        detail.getProductionDate().toLocalDate(),
                        detail.getCost()
                );

        if (existingInventoryOpt.isPresent()) {
            Inventory inventory = existingInventoryOpt.get();
            inventory.setQuantityRemaining(inventory.getQuantityRemaining().add(detail.getQuantity()));
            inventory.setQuantityImported(inventory.getQuantityImported().add(detail.getQuantity()));
            inventoryRepository.save(inventory);
        } else {
            Inventory inventory = new Inventory();
            inventory.setIngredient(ingredient);
            inventory.setCost(detail.getCost());
            inventory.setExpiryDate(detail.getExpiryDate().toLocalDate());
            inventory.setProductionDate(detail.getProductionDate().toLocalDate());
            inventory.setQuantityRemaining(detail.getQuantity());
            inventory.setQuantityImported(detail.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void revertInventoryFromImport(Import importBeforeUpdate) {
        for (ImportDetail detail : importBeforeUpdate.getImportDetails()) {
            revertInventoryFromDetail(detail);
        }
    }

    private void revertInventoryFromDetail(ImportDetail detail) {
        Optional<Inventory> inventoryOpt = inventoryRepository
                .findByIngredientAndExpiryDateAndProductionDateAndCost(
                        detail.getIngredient(),
                        detail.getExpiryDate().toLocalDate(),
                        detail.getProductionDate().toLocalDate(),
                        detail.getCost()
                );

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            BigDecimal quantityUsed = inventory.getQuantityImported().subtract(inventory.getQuantityRemaining());

            if (detail.getQuantity().compareTo(quantityUsed) < 0) {
                throw new IllegalStateException("Không thể xoá vì đã sử dụng vượt quá số lượng nhập.");
            }

            inventory.setQuantityImported(inventory.getQuantityImported().subtract(detail.getQuantity()));
            inventory.setQuantityRemaining(inventory.getQuantityRemaining().subtract(detail.getQuantity()));
            inventoryRepository.save(inventory);
        } else {
            throw new IllegalStateException("Không tìm thấy Inventory để revert.");
        }
    }

    // ------------------ Bếp yêu cầu nguyên liệu ------------------
    public void requestFromInventory(Long ingredientId, BigDecimal quantityNeeded) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu."));

        List<Inventory> inventories = inventoryRepository
                .findByIngredientOrderByExpiryDateAsc(ingredient);

        BigDecimal remaining = quantityNeeded;

        for (Inventory inventory : inventories) {
            if (inventory.isDeleted() || inventory.getQuantityRemaining().compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal available = inventory.getQuantityRemaining();
            BigDecimal toTake = available.min(remaining);

            inventory.setQuantityRemaining(available.subtract(toTake));
            inventoryRepository.save(inventory);

            remaining = remaining.subtract(toTake);
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Không đủ nguyên liệu trong kho.");
        }
    }

    // ------------------ Bếp trả lại nguyên liệu ------------------
    public void returnToInventory(Long ingredientId, BigDecimal quantityReturn) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu."));

        List<Inventory> inventories = inventoryRepository
                .findByIngredientOrderByExpiryDateAsc(ingredient);

        BigDecimal remaining = quantityReturn;

        for (Inventory inventory : inventories) {
            BigDecimal used = inventory.getQuantityImported().subtract(inventory.getQuantityRemaining());

            if (used.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal toReturn = used.min(remaining);
            inventory.setQuantityRemaining(inventory.getQuantityRemaining().add(toReturn));
            inventoryRepository.save(inventory);

            remaining = remaining.subtract(toReturn);
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
        }

        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Không thể trả nhiều hơn lượng đã dùng.");
        }
    }
}
