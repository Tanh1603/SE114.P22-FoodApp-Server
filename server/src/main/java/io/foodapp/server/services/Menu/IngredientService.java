package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<IngredientResponse> getAllIngredients() {
        try {
            return ingredientRepository.findAll().stream()
                    .filter(ingredient -> !ingredient.isDeleted())
                    .map(ingredientResponseMapper::toDTO)
                    .collect(Collectors.toList());
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
            Ingredient ingredient = ingredientRequestMapper.toEntity(request);
    
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unit not found"));
    
            ingredient.setUnit(unit); // Gán unit đã được quản lý
    
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
            Optional<Ingredient> entity = ingredientRepository.findById(id);
            if (entity.isPresent()) {
                entity.get().setDeleted(true);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
