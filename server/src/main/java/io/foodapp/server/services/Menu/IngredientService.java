package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.dtos.Menu.IngredientResponse;
import io.foodapp.server.mappers.Menu.IngredientRequestMapper;
import io.foodapp.server.mappers.Menu.IngredientResponseMapper;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.models.MenuModel.Unit;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Menu.UnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientResponseMapper ingredientResponseMapper;
    private final IngredientRequestMapper ingredientRequestMapper;
    private final UnitRepository unitRepository;

    public List<IngredientResponse> getAvailableIngredients() {
        try {
            return ingredientResponseMapper.toDtoList(ingredientRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<IngredientResponse> getDeletedIngredients() {
        try {
            return ingredientResponseMapper.toDtoList(ingredientRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public IngredientResponse getIngredientById(Long id) {
        try {
            return ingredientResponseMapper.toDTO(ingredientRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
                    return ingredientResponseMapper.toDTO(restored);
                } else {
                    throw new IllegalArgumentException("Ingredient with the same name and unit already exists.");
                }
            }
    
            // Tạo mới Ingredient
            Ingredient ingredient = ingredientRequestMapper.toEntity(request);
    
            // Gán Unit đã được quản lý
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unit not found"));
            ingredient.setUnit(unit);
    
            Ingredient savedIngredient = ingredientRepository.save(ingredient);
            return ingredientResponseMapper.toDTO(savedIngredient);
    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }    


    public IngredientResponse updateIngredient(IngredientRequest request) {
        try {
            Ingredient ingredient = ingredientRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    
            // 1. Cập nhật các trường từ DTO (ngoại trừ unit)
            ingredientRequestMapper.updateEntityFromDto(request, ingredient);
    
            // 2. Gán lại Unit từ DB nếu có unitId
            if (request.getUnitId() != null) {
                Unit unit = unitRepository.findById(request.getUnitId())
                        .orElseThrow(() -> new RuntimeException("Unit not found"));
                ingredient.setUnit(unit);
            }
    
            // 3. Lưu và trả về DTO
            Ingredient saved = ingredientRepository.save(ingredient);
            return ingredientResponseMapper.toDTO(saved);
    
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    

    public boolean deleteIngredient(Long id) {
        try {
            var existingIngredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

            existingIngredient.setDeleted(true);
            ingredientRepository.save(existingIngredient);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public IngredientResponse recoverIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    
        if (!ingredient.isDeleted()) {
            throw new IllegalStateException("Ingredient is not deleted");
        }
    
        ingredient.setDeleted(false);
        return ingredientResponseMapper.toDTO(ingredientRepository.save(ingredient));
    }
    
}
