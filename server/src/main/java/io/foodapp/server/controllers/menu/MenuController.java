package io.foodapp.server.controllers.menu;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Menu.FoodRequest;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Menu.MenuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<PageResponse<MenuResponse>> getMenus(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MenuResponse> menus = menuService.getMenus(pageable);
        return ResponseEntity.ok(PageResponse.fromPage(menus));
    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Integer id, @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.updateMenu(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateMenuStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> status) {
        boolean active = status.get("active");
        menuService.updateMenuActive(id, active);
        return ResponseEntity.noContent().build();
    }

    // Food
    @GetMapping("/{menuId}/foods")
    public ResponseEntity<PageResponse<FoodResponse>> getFoods(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order,
            @PathVariable Integer menuId) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FoodResponse> responses = menuService.getFoodsByMenuId(menuId, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @PostMapping( value = "/{menuId}/foods", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FoodResponse> createFood(@ModelAttribute FoodRequest request, @PathVariable Integer menuId) {
        return ResponseEntity.ok(menuService.createFood(menuId, request));
    }

    @PutMapping(value = "/{menuId}/foods/{foodId}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FoodResponse> updateFood(@ModelAttribute FoodRequest request, @PathVariable Integer menuId, @PathVariable Long foodId) {
        return ResponseEntity.ok(menuService.updateFood(menuId, foodId, request));
    }

    @PatchMapping("/foods/{foodId}/status")
    public ResponseEntity<Void> updateFoodStatus(@PathVariable Long foodId, @RequestBody Map<String, Boolean> status) {
        boolean active = status.get("active");
        menuService.updateFoodActive(foodId, active);
        return ResponseEntity.noContent().build();
    }
}
