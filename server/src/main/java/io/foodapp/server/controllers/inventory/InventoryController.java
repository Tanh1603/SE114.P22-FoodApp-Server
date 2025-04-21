package io.foodapp.server.controllers.Inventory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Filter.InventoryFilter;
import io.foodapp.server.dtos.Inventory.InventoryRequest;
import io.foodapp.server.dtos.Inventory.InventoryResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Inventory.InventoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<PageResponse<InventoryResponse>> getInventories(
            @ModelAttribute InventoryFilter inventoryFilter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "expiryDate", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryResponse> inventories = inventoryService.getInventories(inventoryFilter, pageable);

        PageResponse<InventoryResponse> response = PageResponse.<InventoryResponse>builder()
                .content(inventories.getContent())
                .page(inventories.getNumber())
                .size(inventories.getSize())
                .totalElements(inventories.getTotalElements())
                .totalPages(inventories.getTotalPages())
                .last(inventories.isLast())
                .first(inventories.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/request/{id}")
    public ResponseEntity<InventoryResponse> requestFromInventory(@PathVariable Long id,
            @RequestBody InventoryRequest inventoryRequest) {
        InventoryResponse inventoryResponse = inventoryService.requestFromInventory(id, inventoryRequest);
        return ResponseEntity.ok(inventoryResponse);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<InventoryResponse> returnToInventory(@PathVariable Long id,
            @RequestBody InventoryRequest inventoryRequest) {
        InventoryResponse inventoryResponse = inventoryService.returnToInventory(id, inventoryRequest);
        return ResponseEntity.ok(inventoryResponse);
    }
}
