package io.foodapp.server.controllers.Inventory;

import io.foodapp.server.dtos.Inventory.SupplierRequest;
import io.foodapp.server.dtos.Inventory.SupplierResponse;
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
    public ResponseEntity<List<SupplierResponse>> getAvailableSuppliers() {
        List<SupplierResponse> suppliers = supplierService.getAvailableSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<SupplierResponse>> getDeletedSuppliers() {
        List<SupplierResponse> suppliers = supplierService.getDeletedSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);

    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierRequest request) {
        SupplierResponse created = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@Valid @PathVariable Long id , @RequestBody SupplierRequest supplierDTO) {
        SupplierResponse updated = supplierService.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();

    }
}
