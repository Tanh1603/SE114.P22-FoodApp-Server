package io.foodapp.server.controllers.Order;

import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Order.FoodTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/coffee-tables")
@RequiredArgsConstructor
public class FoodTableController {

    private final FoodTableService coffeeTableService;
    private final FoodTableService foodTableService;
    // Add methods to handle HTTP requests for coffee tables

    @GetMapping
    public ResponseEntity<PageResponse<FoodTableResponse>> getCoffeeTable(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FoodTableResponse> foodTables = foodTableService.getCoffeeTables(pageable);
        return ResponseEntity.ok(PageResponse.fromPage(foodTables));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodTableResponse> createCoffeeTable(@RequestBody FoodTableRequest request) {
        return ResponseEntity.ok(coffeeTableService.createCoffeeTable(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FoodTableResponse> updateCoffeeTable(@RequestBody FoodTableRequest request, @PathVariable Integer id) {
        return ResponseEntity.ok(coffeeTableService.updateCoffeeTable(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateCoffeeTableStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> request) {
        boolean status = request.get("status");
        foodTableService.setCoffeeTableStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCoffeeTable(@PathVariable Integer id) {
        coffeeTableService.deleteCoffeeTable(id);
        return ResponseEntity.noContent().build();
    }
}
