// package io.foodapp.server.controllers.menu;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.JsonMappingException;

// import io.foodapp.server.dtos.Filter.MenuItemFilter;
// import io.foodapp.server.dtos.Menu.MenuItemRequest;
// import io.foodapp.server.dtos.Menu.MenuItemResponse;
// import io.foodapp.server.dtos.Staff.StaffDTO;
// import io.foodapp.server.dtos.responses.PageResponse;
// import io.foodapp.server.services.Menu.MenuItemService;
// import io.foodapp.server.utils.ValidationUtils;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/v1/menu-items")
// public class MenuItemController {
//     private final MenuItemService menuItemService;

//     @GetMapping
//     public ResponseEntity<PageResponse<MenuItemResponse>> getMenuItems(
//             @ModelAttribute MenuItemFilter menuItemFilter,
//             @RequestParam(defaultValue = "0", required = false) int page,
//             @RequestParam(defaultValue = "10", required = false) int size,
//             @RequestParam(defaultValue = "id", required = false) String sortBy,
//             @RequestParam(defaultValue = "asc", required = false) String order) {

//         Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
//         Pageable pageable = PageRequest.of(page, size, sort);
//         Page<MenuItemResponse> menuItems = menuItemService.getMenuItems(menuItemFilter, pageable);
//         PageResponse<MenuItemResponse> response = PageResponse.<MenuItemResponse>builder()
//                                                 .content(menuItems.getContent())
//                                                 .page(menuItems.getNumber())
//                                                 .size(menuItems.getSize())
//                                                 .totalElements(menuItems.getTotalElements())
//                                                 .totalPages(menuItems.getTotalPages())
//                                                 .last(menuItems.isLast())
//                                                 .first(menuItems.isFirst())
//                                                 .build();
//         return ResponseEntity.ok(response);
//     }

//     @PostMapping
//     public ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody MenuItemRequest request) {
//         MenuItemResponse menuItemResponse = menuItemService.createMenuItem(request);
//         return ResponseEntity.ok(menuItemResponse);
//     }


    



//     @PutMapping("/{id}")
//     public ResponseEntity<MenuItemResponse> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemRequest request) {
//         MenuItemResponse menuItemResponse = menuItemService.updateImport(id, request);
//         return ResponseEntity.ok(menuItemResponse);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteImport(@PathVariable Long id) {
//         menuItemService.deleteImport(id);
//         return ResponseEntity.noContent().build();
//     }
// }
