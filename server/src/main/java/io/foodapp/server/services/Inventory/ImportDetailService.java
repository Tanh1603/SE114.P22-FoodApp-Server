package io.foodapp.server.services.Inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Inventory.ImportDetailRequest;
import io.foodapp.server.dtos.Inventory.ImportDetailResponse;
import io.foodapp.server.mappers.Inventory.ImportDetailRequestMapper;
import io.foodapp.server.mappers.Inventory.ImportDetailResponseMapper;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.repositories.Inventory.ImportDetailRepository;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportDetailService {
    public final ImportDetailRepository importDetailRepository;
    public final ImportDetailResponseMapper importDetailResponseMapper;
    public final ImportDetailRequestMapper importDetailRequestMapper;
    public final IngredientRepository ingredientRepository;

    public List<ImportDetailResponse> getAllImportDetails() {
        try {
            return importDetailRepository.findAll().stream()
                    .map(importDetailResponseMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ImportDetailResponse getImportDetailById(Long id) {
        try {
            return importDetailRepository.findById(id)
                .map(importDetailResponseMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("ImportDetail not found with id: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ImportDetailResponse updateImportDetail(ImportDetailRequest request) {
        try {
            // Tìm ImportDetail cần cập nhật
            ImportDetail importDetail = importDetailRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("ImportDetail not found!"));
    
            // Cập nhật các field đơn (ngoại trừ ingredient)
            importDetailRequestMapper.updateEntityFromDto(request, importDetail);
    
            // Tìm và gán Ingredient mới (nếu cần)
            if (request.getIngredientId() != null) {
                Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                        .orElseThrow(() -> new RuntimeException("Ingredient not found!"));
                importDetail.setIngredient(ingredient);
            }
    
            // Lưu lại ImportDetail đã cập nhật
            ImportDetail updatedImportDetail = importDetailRepository.save(importDetail);
            return importDetailResponseMapper.toDTO(updatedImportDetail);
    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    public boolean deleteImportDetail(Long id) {
        try {
            importDetailRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
