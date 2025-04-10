package io.foodapp.server.controllers.Inventory;

import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.services.Inventory.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/import")
public class ImportController {
    private final ImportService importService;

    @GetMapping
    public ResponseEntity<List<ImportResponse>> getAllImports() {
        List<ImportResponse> imports = importService.getAllImports();
        return ResponseEntity.ok(imports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImportById(@PathVariable Long id) {
        try {
            ImportResponse result = importService.getImportById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Import not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createImport(@RequestBody ImportRequest request) {
        try {
            ImportResponse created = importService.createImport(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Failed to create import: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateImport(@RequestBody ImportRequest request) {
        try {
            ImportResponse updated = importService.updateImport(request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Failed to update import: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImport(@PathVariable Long id) {
        try {
            importService.deleteImport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete import: " + e.getMessage()));
        }
    }
}
