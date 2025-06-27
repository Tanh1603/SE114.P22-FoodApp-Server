package io.foodapp.server.controllers.User;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.auth.UserRecord;

import io.foodapp.server.services.User.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<UserRecord>> listAllCustomers() {
        return ResponseEntity.ok(customerService.listAllUsers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<UserRecord> getCustomerDetails(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomerDetails(customerId));
    }

    @PatchMapping(value = "/{customerId}/avatar", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<UserRecord> updateCustomerPhoto(@PathVariable String customerId,
            @RequestParam MultipartFile avatar) {
        return ResponseEntity.ok(customerService.updatePhotoUrl(customerId, avatar));
    }
}
