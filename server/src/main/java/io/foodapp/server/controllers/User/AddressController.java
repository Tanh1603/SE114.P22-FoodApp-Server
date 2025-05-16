package io.foodapp.server.controllers.User;

import io.foodapp.server.dtos.User.AddressRequest;
import io.foodapp.server.dtos.User.AddressResponse;
import io.foodapp.server.services.User.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {
    private final AddressService customerService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses() {
        return ResponseEntity.ok(customerService.getAddresses());
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody AddressRequest request) {
        AddressResponse response = customerService.createAddress(request);
        URI location = URI.create("/api/v1/addresses/" + response.getId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddresses(@RequestBody AddressRequest request, @PathVariable Long id) {
        AddressResponse response = customerService.updateAddress(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddresses(@PathVariable Long id) {
        customerService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
