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
        List<IngredientResponse> ingredients = ingredientService.getAvailableIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<IngredientResponse>> getDeletedIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getDeletedIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Long id) {
        IngredientResponse ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping
    public ResponseEntity<?> createIngredient(@RequestBody IngredientRequest request) {
        IngredientResponse created = ingredientService.createIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIngredient(@ PathVariable Long id, @RequestBody IngredientRequest request) {
        IngredientResponse updated = ingredientService.updateIngredient(id, request);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build(); // 204 No Content

    }

    @PutMapping("/recover/{id}")
    public ResponseEntity<?> recoverIngredient(@PathVariable Long id) {
        IngredientResponse recovered = ingredientService.recoverIngredient(id);
        return ResponseEntity.ok(recovered); // 200 OK + body
    }

}
