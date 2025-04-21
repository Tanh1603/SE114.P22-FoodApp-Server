package io.foodapp.server.controllers.Inventory;

import io.foodapp.server.dtos.responses.PageResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.foodapp.server.dtos.Filter.ExportFilter;
import io.foodapp.server.dtos.Inventory.ExportRequest;
import io.foodapp.server.dtos.Inventory.ExportResponse;
import io.foodapp.server.services.Inventory.ExportService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exports")
public class ExportController {
    private final ExportService exportService;

    @GetMapping
    public ResponseEntity<PageResponse<ExportResponse>> getExports(
            @ModelAttribute ExportFilter exportFilter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ExportResponse> exports = exportService.getExports(exportFilter, pageable);
        PageResponse<ExportResponse> response = PageResponse.<ExportResponse>builder()
                                                .content(exports.getContent())
                                                .page(exports.getNumber())
                                                .size(exports.getSize())
                                                .totalElements(exports.getTotalElements())
                                                .totalPages(exports.getTotalPages())
                                                .last(exports.isLast())
                                                .first(exports.isFirst())
                                                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ExportResponse> createExport(@RequestBody ExportRequest exportRequest) {
        ExportResponse exportResponse = exportService.createExport(exportRequest);
        return ResponseEntity.ok(exportResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExportResponse> updateExport(@PathVariable Long id, @RequestBody ExportRequest exportRequest) {
        ExportResponse exportResponse = exportService.updateExport(id, exportRequest);
        return ResponseEntity.ok(exportResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExport(@PathVariable Long id) {
        exportService.deleteExport(id);
        return ResponseEntity.noContent().build();
    }
}
