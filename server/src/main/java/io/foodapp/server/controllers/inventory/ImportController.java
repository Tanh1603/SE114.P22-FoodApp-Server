package io.foodapp.server.controllers.Inventory;

import io.foodapp.server.dtos.Filter.ImportFilter;
import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Inventory.ImportService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/imports")
public class ImportController {
    private final ImportService importService;

    @GetMapping
    public ResponseEntity<PageResponse<ImportResponse>> getImports(
            @ModelAttribute ImportFilter importFilter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ImportResponse> imports = importService.getImports(importFilter, pageable);
        PageResponse<ImportResponse> response = PageResponse.<ImportResponse>builder()
                                                .content(imports.getContent())
                                                .page(imports.getNumber())
                                                .size(imports.getSize())
                                                .totalElements(imports.getTotalElements())
                                                .totalPages(imports.getTotalPages())
                                                .last(imports.isLast())
                                                .first(imports.isFirst())
                                                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ImportResponse> createImport(@RequestBody ImportRequest importRequest) {
        ImportResponse importResponse = importService.createImport(importRequest);
        return ResponseEntity.ok(importResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportResponse> updateImport(@PathVariable Long id, @RequestBody ImportRequest importRequest) {
        ImportResponse importResponse = importService.updateImport(id, importRequest);
        return ResponseEntity.ok(importResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImport(@PathVariable Long id) {
        importService.deleteImport(id);
        return ResponseEntity.noContent().build();
    }
}
