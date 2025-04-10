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

@RestController
@RequestMapping("/api/v1/unit")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        List<UnitDTO> units = unitService.getAllUnits();
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
                Map<String, String> error = new HashMap<>();
                error.put("message", "Unit not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error occurred while deleting unit: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
}
