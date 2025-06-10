package io.foodapp.server.controllers.Order;

import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Order.FoodTableService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import io.foodapp.server.dtos.Filter.FoodTableFilter;
import io.foodapp.server.dtos.Filter.PageFilter;

@RestController
@RequestMapping("/api/v1/food-tables")
@RequiredArgsConstructor
public class FoodTableController {

    private final FoodTableService FoodTableService;
    private final FoodTableService foodTableService;
    // Add methods to handle HTTP requests for Food tables

    @GetMapping
    public ResponseEntity<PageResponse<FoodTableResponse>> getFoodTable(
            @ModelAttribute PageFilter pageFilter,
            @ModelAttribute FoodTableFilter foodTableFilter) {
        Page<FoodTableResponse> foodTables = foodTableService.getFoodTables(PageFilter.toPageAble(pageFilter),
                foodTableFilter);
        return ResponseEntity.ok(PageResponse.fromPage(foodTables));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodTableResponse> createFoodTable(@RequestBody FoodTableRequest request) {
        return ResponseEntity.ok(FoodTableService.createFoodTable(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodTableResponse> updateFoodTable(@RequestBody FoodTableRequest request,
            @PathVariable Integer id) {
        return ResponseEntity.ok(FoodTableService.updateFoodTable(id, request));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateFoodTableStatus(@PathVariable Integer id) {
        foodTableService.setFoodTableStatus(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteFoodTable(@PathVariable Integer id) {
        FoodTableService.deleteFoodTable(id);
        return ResponseEntity.noContent().build();
    }
}
