package io.foodapp.server.controllers.menu;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.services.Menu.MenuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    // Add methods to handle HTTP requests for coffee tables

    @GetMapping("/active")
    public ResponseEntity<List<MenuResponse>> getActiveMenus() {
        return ResponseEntity.ok(menuService.getActiveMenus());
    }

    @GetMapping("/inActive")
    public ResponseEntity<List<MenuResponse>> getInActiveMenu() {
        return ResponseEntity.ok(menuService.getInActiveMenus());
    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.createMenu(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> updateMenu(@RequestBody MenuRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(menuService.updateMenu(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-active/{id}")
    public ResponseEntity<?> recoverIngredient(@PathVariable Long id, @RequestBody boolean isActive) {
        menuService.setMenuActive(id, isActive);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
