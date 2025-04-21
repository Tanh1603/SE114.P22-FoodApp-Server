package io.foodapp.server.controllers.Inventory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Filter.SupplierFilter;
import io.foodapp.server.dtos.Inventory.SupplierRequest;
import io.foodapp.server.dtos.Inventory.SupplierResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Inventory.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<PageResponse<SupplierResponse>> getSuppliers(
            @ModelAttribute SupplierFilter supplierFilter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierResponse> suppliers = supplierService.getSuppliers(supplierFilter, pageable);

        PageResponse<SupplierResponse> response = PageResponse.<SupplierResponse>builder()
                .content(suppliers.getContent())
                .page(suppliers.getNumber())
                .size(suppliers.getSize())
                .totalElements(suppliers.getTotalElements())
                .totalPages(suppliers.getTotalPages())
                .last(suppliers.isLast())
                .first(suppliers.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierRequest request) {
        SupplierResponse created = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@Valid @PathVariable Long id, @RequestBody SupplierRequest supplierDTO) {
        SupplierResponse updated = supplierService.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(updated);

    }

    @PutMapping("/set-active/{id}")
    public ResponseEntity<?> setActiveSupplier(@PathVariable Long id, @RequestBody boolean isActive) {
        supplierService.setActiveSupplier(id, isActive);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();

    }
}
