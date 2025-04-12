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
@RequestMapping("/api/v1/imports")
public class ImportController {
    private final ImportService importService;

    @GetMapping("/available")
    public ResponseEntity<List<ImportResponse>> getAvailableImports() {
        List<ImportResponse> imports = importService.getAvailableImports();
        return ResponseEntity.ok(imports);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<ImportResponse>> getDeletedImports() {
        List<ImportResponse> imports = importService.getDeletedImports();
        return ResponseEntity.ok(imports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImportById(@PathVariable Long id) {
        ImportResponse result = importService.getImportById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createImport(@RequestBody ImportRequest request) {
        ImportResponse created = importService.createImport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping
    public ResponseEntity<?> updateImport(@RequestBody ImportRequest request) {
        ImportResponse updated = importService.updateImport(request);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImport(@PathVariable Long id) {
        importService.deleteImport(id);
        return ResponseEntity.noContent().build();

    }
}
