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

import io.foodapp.server.dtos.Menu.UnitRequest;
import io.foodapp.server.dtos.Menu.UnitResponse;
import io.foodapp.server.services.Menu.UnitService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping("/active")
    public ResponseEntity<List<UnitResponse>> getActiveUnits() {
        List<UnitResponse> units = unitService.getActiveUnits();
        return ResponseEntity.ok(units);
    }

    @GetMapping("/inActive")
    public ResponseEntity<List<UnitResponse>> getDeletedUnits() {
        List<UnitResponse> units = unitService.getInActiveUnits();

        return ResponseEntity.ok(units);
    }

    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody UnitRequest unitDTO) {
        UnitResponse created = unitService.createUnit(unitDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @RequestBody UnitRequest unitDTO) {
        UnitResponse updated = unitService.updateUnit(id, unitDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-active/{id}")
    public ResponseEntity<?> recoverUnit(@PathVariable Long id, @RequestBody boolean isActive) {
        unitService.setActiveUnit(id, isActive);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
