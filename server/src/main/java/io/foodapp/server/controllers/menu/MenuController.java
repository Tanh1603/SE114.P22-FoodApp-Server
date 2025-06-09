package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.services.Menu.MenuService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(menuService.getMenus(status, name));
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
    public ResponseEntity<Void> updateMenuStatus(@PathVariable Integer id) {
        menuService.updateMenuActive(id);
        return ResponseEntity.noContent().build();
    }


}
