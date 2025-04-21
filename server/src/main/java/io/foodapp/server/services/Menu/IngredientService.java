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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final UnitRepository unitRepository;

    public List<IngredientResponse> getActiveIngredients() {
        try {
            return ingredientMapper.toDTOs(ingredientRepository.findByIsActiveTrue());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public List<IngredientResponse> getInActiveIngredients() {
        try {
            return ingredientMapper.toDTOs(ingredientRepository.findByIsActiveFalse());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    public IngredientResponse getIngredientById(Long id) {
        try {
            return ingredientMapper.toDTO(ingredientRepository.findById(id).orElseThrow());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    @Transactional
    public IngredientResponse createIngredient(IngredientRequest request) {
        try {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findByNameAndUnit_Id(request.getName(), request.getUnitId());
    
            if (optionalIngredient.isPresent()) {
                Ingredient existingIngredient = optionalIngredient.get();
    
                if (!existingIngredient.isActive()) {
                    existingIngredient.setActive(true);
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
    
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }    

    @Transactional
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
    
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());

        }
    }
    
    
    @Transactional
    public void deleteIngredient(Long id) {
        try {
            var existingIngredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

            if (existingIngredient.getInventories() != null && !existingIngredient.getInventories().isEmpty() &&
                    existingIngredient.getImportDetails() != null && !existingIngredient.getImportDetails().isEmpty()) {
                    throw new RuntimeException("Cannot delete ingredient because it is being used by some menu items.");
                }
                
            ingredientRepository.delete(existingIngredient);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }

    @Transactional
    public void setIngredientActive(Long id, boolean isActive) {
        try {
            Ingredient ingredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
            ingredient.setActive(isActive);
            ingredientRepository.save(ingredient);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ingredient data: " + e.getMessage());
        }
    }
    
}
