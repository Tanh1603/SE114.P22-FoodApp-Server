package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.dtos.Menu.IngredientResponse;
import io.foodapp.server.mappers.Menu.IngredientMapper;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.models.MenuModel.Unit;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Menu.UnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final UnitRepository unitRepository;

    public List<IngredientResponse> getAvailableIngredients() {
        try {
            return ingredientMapper.toDTOs(ingredientRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public List<IngredientResponse> getDeletedIngredients() {
        try {
            return ingredientMapper.toDTOs(ingredientRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public IngredientResponse getIngredientById(Long id) {
        try {
            return ingredientMapper.toDTO(ingredientRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        try {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByNameAndUnit_Id(request.getName(), request.getUnitId());
    
            if (optionalIngredient.isPresent()) {
                Ingredient existingIngredient = optionalIngredient.get();
    
                if (existingIngredient.isDeleted()) {
                    existingIngredient.setDeleted(false);
                    Ingredient restored = ingredientRepository.save(existingIngredient);
                    return ingredientMapper.toDTO(restored);
                } else {
                    throw new IllegalArgumentException("Ingredient with the same name and unit already exists.");
                }
            }
    
            // Tạo mới Ingredient
            Ingredient ingredient = ingredientMapper.toEntity(request);
    
            // Gán Unit đã được quản lý
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unit not found"));
            ingredient.setUnit(unit);
    
            Ingredient savedIngredient = ingredientRepository.save(ingredient);
            return ingredientMapper.toDTO(savedIngredient);
    
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }    


    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        try {
            Ingredient ingredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    
            // 1. Cập nhật các trường từ DTO (ngoại trừ unit)
            ingredientMapper.updateEntityFromDto(request, ingredient);
    
            // 2. Gán lại Unit từ DB nếu có unitId
            if (request.getUnitId() != null) {
                Unit unit = unitRepository.findById(request.getUnitId())
                        .orElseThrow(() -> new RuntimeException("Unit not found"));
                ingredient.setUnit(unit);
            }
    
            // 3. Lưu và trả về DTO
            Ingredient saved = ingredientRepository.save(ingredient);
            return ingredientMapper.toDTO(saved);
    
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());

        }
    }
    
    

    public void deleteIngredient(Long id) {
        try {
            var existingIngredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

            existingIngredient.setDeleted(true);
            ingredientRepository.save(existingIngredient);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public IngredientResponse recoverIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    
        if (!ingredient.isDeleted()) {
            throw new IllegalStateException("Ingredient is not deleted");
        }
    
        ingredient.setDeleted(false);
        return ingredientMapper.toDTO(ingredientRepository.save(ingredient));
    }
    
}
