package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Menu.UnitDTO;
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
    public ResponseEntity<List<UnitDTO>> getAvailableUnits() {
        List<UnitDTO> units = unitService.getAvailableUnits();
        return ResponseEntity.ok(units);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UnitDTO>> getDeletedUnits() {
        List<UnitDTO> units = unitService.getDeletedUnits();

        return ResponseEntity.ok(units);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUnitById(@PathVariable Long id) {
        UnitDTO unit = unitService.getUnitById(id);

        return ResponseEntity.ok(unit);
    }

    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody UnitDTO unitDTO) {
        UnitDTO created = unitService.createUnit(unitDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @RequestBody UnitDTO unitDTO) {
        unitDTO.setId(id);
        UnitDTO updated = unitService.updateUnit(unitDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/recover/{id}")
    public ResponseEntity<?> recoverUnit(@PathVariable Long id) {
        UnitDTO recovered = unitService.recoverUnit(id);
        return ResponseEntity.ok(recovered);
    }

}
