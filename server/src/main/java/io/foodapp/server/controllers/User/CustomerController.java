package io.foodapp.server.controllers.User;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.User.AddressRequest;
import io.foodapp.server.dtos.User.AddressResponse;
import io.foodapp.server.dtos.User.VoucherResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Menu.MenuService;
import io.foodapp.server.services.User.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final MenuService menuService;


    @PostMapping("/{customerId}/vouchers/{voucherId}")
    public ResponseEntity<VoucherResponse> receiveVoucher(@PathVariable String customerId, @PathVariable Long voucherId) {
        return ResponseEntity.ok(customerService.receiveVoucher(customerId, voucherId));
    }

    @GetMapping("/{customerId}/vouchers")
    public ResponseEntity<PageResponse<VoucherResponse>> getVouchersByCustomerId(
            @PathVariable String customerId,
            @ModelAttribute PageFilter filter) {
        Pageable pageable = PageFilter.toPageAble(filter);
        PageResponse<VoucherResponse> response = PageResponse
                .fromPage(customerService.getVoucherByUserId(customerId, pageable));
        return ResponseEntity.ok(response);
    }

    // api address
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponse>> getAddressesByUserId(@PathVariable String userId) {
        List<AddressResponse> addresses = customerService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponse> createAddress(@PathVariable String userId, @RequestBody AddressRequest request) {
        AddressResponse createdAddress = customerService.createAddress(userId, request);
        return ResponseEntity.ok(createdAddress);
    }

    @PutMapping("/{userId}/addresses/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable String userId, @PathVariable Long id, @RequestBody AddressRequest request) {
        AddressResponse updatedAddress = customerService.updateAddress(userId, id, request);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{userId}/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String userId, @PathVariable Long id) {
        customerService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }

    // food
    @PostMapping("/{userId}/foods/{foodId}/status")
    public ResponseEntity<Void> toggleFoodLikeStatus(@PathVariable String userId, @PathVariable Long foodId) {
        menuService.toggleFoodLikeStatus(userId, foodId);
        return ResponseEntity.noContent().build();
    }

}
