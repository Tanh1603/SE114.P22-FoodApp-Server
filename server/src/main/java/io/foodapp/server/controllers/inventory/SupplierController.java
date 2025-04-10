package io.foodapp.server.controllers.Inventory;

import io.foodapp.server.dtos.Inventory.SupplierDTO;
import io.foodapp.server.services.Inventory.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        try {
            SupplierDTO supplier = supplierService.getSupplierById(id);
            return ResponseEntity.ok(supplier);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Supplier not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO created = supplierService.createSupplier(supplierDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Failed to create supplier: " + e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO updated = supplierService.updateSupplier(supplierDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Failed to update supplier: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete supplier: " + e.getMessage()));
        }
    }
}
