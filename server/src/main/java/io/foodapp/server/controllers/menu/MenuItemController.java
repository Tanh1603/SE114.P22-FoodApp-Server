 package io.foodapp.server.controllers.menu;

 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.PageRequest;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.domain.Sort;
 import org.springframework.http.MediaType;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.DeleteMapping;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.ModelAttribute;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.PutMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;

 import io.foodapp.server.dtos.Filter.MenuItemFilter;
 import io.foodapp.server.dtos.Menu.MenuItemRequest;
 import io.foodapp.server.dtos.Menu.MenuItemResponse;
 import io.foodapp.server.dtos.responses.PageResponse;
 import io.foodapp.server.services.Menu.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/api/v1/menu-items")
 public class MenuItemController {
     private final MenuItemService menuItemService;

     @GetMapping
     public ResponseEntity<PageResponse<MenuItemResponse>> getMenuItems(
             @ModelAttribute MenuItemFilter menuItemFilter,
             @RequestParam(defaultValue = "0", required = false) int page,
             @RequestParam(defaultValue = "10", required = false) int size,
             @RequestParam(defaultValue = "id", required = false) String sortBy,
             @RequestParam(defaultValue = "asc", required = false) String order) {

         Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
         Pageable pageable = PageRequest.of(page, size, sort);
         Page<MenuItemResponse> menuItems = menuItemService.getMenuItems(menuItemFilter, pageable);
         PageResponse<MenuItemResponse> response = PageResponse.<MenuItemResponse>builder()
                                                 .content(menuItems.getContent())
                                                 .page(menuItems.getNumber())
                                                 .size(menuItems.getSize())
                                                 .totalElements(menuItems.getTotalElements())
                                                 .totalPages(menuItems.getTotalPages())
                                                 .last(menuItems.isLast())
                                                 .first(menuItems.isFirst())
                                                 .build();
         return ResponseEntity.ok(response);
     }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemDetailById(@PathVariable Long id) {
        MenuItemResponse menuItem = menuItemService.getMenuItemDetailById(id);
        return ResponseEntity.ok(menuItem);
    }

    @PostMapping(consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItemResponse> createMenuItem(@Valid @ModelAttribute MenuItemRequest request) {
        MenuItemResponse createdMenuItem = menuItemService.createMenuItem(request);
        return ResponseEntity.ok(createdMenuItem);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @Valid @ModelAttribute MenuItemRequest request,
            @PathVariable Long id) {
        MenuItemResponse updateMenuItem = menuItemService.updateMenuItem(id, request);
        return ResponseEntity.ok(updateMenuItem);
    }


     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
         menuItemService.deleteMenuItem(id);
         return ResponseEntity.noContent().build();
     }
 }
