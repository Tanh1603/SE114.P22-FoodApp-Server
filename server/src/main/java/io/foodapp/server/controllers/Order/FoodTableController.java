package io.foodapp.server.controllers.Order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.services.Order.FoodTableService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/coffee-tables")
@RequiredArgsConstructor
public class FoodTableController {
    
    private final FoodTableService coffeeTableService;
    // Add methods to handle HTTP requests for coffee tables

    @GetMapping("/available")
    public ResponseEntity<List<FoodTableResponse>> getAvailableCoffeTable() {
        return ResponseEntity.ok(coffeeTableService.getCoffeTablesAvailable());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<FoodTableResponse>> getDeletedCoffeTable() {
        return ResponseEntity.ok(coffeeTableService.getCoffeTablesDeleted());
    }

    @PostMapping
    public ResponseEntity<FoodTableResponse> createCoffeeTable(@RequestBody FoodTableRequest request) {
        return ResponseEntity.ok(coffeeTableService.createCoffeeTable(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FoodTableResponse> updateCoffeeTable(@RequestBody FoodTableRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(coffeeTableService.updateCoffeeTable(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeTable(@PathVariable Long id) {
        coffeeTableService.deleteCoffeeTable(id);
        return ResponseEntity.noContent().build();
    }
}
