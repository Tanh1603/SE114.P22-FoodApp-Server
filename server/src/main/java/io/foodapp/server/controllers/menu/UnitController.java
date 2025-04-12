package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Menu.UnitDTO;
import io.foodapp.server.services.Menu.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
        try {
            UnitDTO unit = unitService.getUnitById(id);
            return ResponseEntity.ok(unit);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unit not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    @PostMapping
    public ResponseEntity<?> createUnit(@RequestBody UnitDTO unitDTO) {
        try {
            UnitDTO created = unitService.createUnit(unitDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to create unit: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUnit(@PathVariable Long id, @RequestBody UnitDTO unitDTO) {
        try {
            unitDTO.setId(id); // đảm bảo ID trong DTO đúng
            UnitDTO updated = unitService.updateUnit(unitDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update unit: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUnit(@PathVariable Long id) {
        try {
            boolean deleted = unitService.deleteUnit(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Unit not found or already deleted."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting Unit: " + e.getMessage()));
        }
    }

    @PutMapping("/recover/{id}")
    public ResponseEntity<?> recoverUnit(@PathVariable Long id) {
        try {
            UnitDTO recovered = unitService.recoverUnit(id);
            return ResponseEntity.ok(recovered);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Unit not found with id: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error recovering unit: " + e.getMessage()));
        }
    }

    
}
