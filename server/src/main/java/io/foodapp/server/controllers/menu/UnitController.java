package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Menu.UnitRequest;
import io.foodapp.server.dtos.Menu.UnitResponse;
import io.foodapp.server.services.Menu.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping("/available")
    public ResponseEntity<List<UnitResponse>> getAvailableUnits() {
        List<UnitResponse> units = unitService.getAvailableUnits();
        return ResponseEntity.ok(units);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UnitResponse>> getDeletedUnits() {
        List<UnitResponse> units = unitService.getDeletedUnits();

        return ResponseEntity.ok(units);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUnitById(@PathVariable Long id) {
        UnitResponse unit = unitService.getUnitById(id);

        return ResponseEntity.ok(unit);
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

    @PutMapping("/recover/{id}")
    public ResponseEntity<?> recoverUnit(@PathVariable Long id) {
        unitService.recoverUnit(id);
        return ResponseEntity.ok().build();
    }

}
