package io.foodapp.server.controllers.menu;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.dtos.Menu.IngredientResponse;
import io.foodapp.server.services.Menu.IngredientService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/active")
    public ResponseEntity<List<IngredientResponse>> getActiveIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getActiveIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/inActive")
    public ResponseEntity<List<IngredientResponse>> getInActiveIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getInActiveIngredients();
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

    @PutMapping("/set-active/{id}")
    public ResponseEntity<?> recoverIngredient(@PathVariable Long id, @RequestBody boolean isActive) {
        ingredientService.setIngredientActive(id, isActive);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
