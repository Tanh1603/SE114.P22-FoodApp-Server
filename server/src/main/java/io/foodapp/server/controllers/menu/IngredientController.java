package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.dtos.Menu.IngredientResponse;
import io.foodapp.server.services.Menu.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/available")
    public ResponseEntity<List<IngredientResponse>> getAvailableIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getAvailableIngredients( );
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<IngredientResponse>> getDeletedIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getDeletedIngredients( );
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Long id) {
        try {
            IngredientResponse ingredient = ingredientService.getIngredientById(id);
            return ResponseEntity.ok(ingredient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Ingredient not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createIngredient(@RequestBody IngredientRequest request) {
        try {
            IngredientResponse created = ingredientService.createIngredient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Failed to create ingredient: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateIngredient(@RequestBody IngredientRequest request) {
        try {
            IngredientResponse updated = ingredientService.updateIngredient(request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Failed to update ingredient: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        try {
            boolean deleted = ingredientService.deleteIngredient(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Unit not found or already deleted."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting Unit: " + e.getMessage()));
        }
    }

    @PutMapping("/recover/{id}")
    public ResponseEntity<?> recoverIngredient(@PathVariable Long id) {
        try {
            IngredientResponse recovered = ingredientService.recoverIngredient(id);
            return ResponseEntity.ok(recovered); // 200 OK + body
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ingredient not found with id: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error recovering ingredient: " + e.getMessage()));
        }
    }

}
