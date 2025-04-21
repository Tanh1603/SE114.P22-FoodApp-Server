package io.foodapp.server.services.Inventory;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.InventoryFilter;
import io.foodapp.server.dtos.Inventory.InventoryRequest;
import io.foodapp.server.dtos.Inventory.InventoryResponse;
import io.foodapp.server.dtos.Specification.InventorySpecification;
import io.foodapp.server.mappers.Inventory.InventoryMapper;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.InventoryModel.Inventory;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.repositories.Inventory.InventoryRepository;
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
                        detail.getCost()
                );

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
                        detail.getCost()
                );

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


    // ------------------ Bếp yêu cầu nguyên liệu ------------------
    public InventoryResponse requestFromInventory(Long id, InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu."));

        if(inventory.getQuantityRemaining().compareTo(inventoryRequest.getQuantity()) < 0) {
            throw new IllegalStateException("Không đủ nguyên liệu trong kho.");
        }
        inventory.setQuantityRemaining(inventory.getQuantityRemaining().subtract(inventoryRequest.getQuantity()));
        return inventoryMapper.toDTO(inventoryRepository.save(inventory));
    }

    // ------------------ Bếp trả lại nguyên liệu ------------------
    public InventoryResponse returnToInventory(Long id, InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu."));

        if(inventoryRequest.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số lượng trả lại không hợp lệ.");
        }

        inventory.setQuantityRemaining(inventory.getQuantityRemaining().add(inventoryRequest.getQuantity()));
        inventory.setOutOfStock(false);
        return inventoryMapper.toDTO(inventoryRepository.save(inventory));
        
    }
}
