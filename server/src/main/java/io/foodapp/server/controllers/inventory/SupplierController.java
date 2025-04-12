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
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/available")
    public ResponseEntity<List<SupplierDTO>> getAvailableSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAvailableSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<SupplierDTO>> getDeletedSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getDeletedSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        SupplierDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);

    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO created = supplierService.createSupplier(supplierDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping
    public ResponseEntity<?> updateSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        SupplierDTO updated = supplierService.updateSupplier(supplierDTO);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build(); // 204 No Content

    }
}
