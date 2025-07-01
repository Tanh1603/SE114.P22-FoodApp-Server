package io.foodapp.server.controllers.User;

import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.Filter.VoucherFilter;
import io.foodapp.server.dtos.User.VoucherRequest;
import io.foodapp.server.dtos.User.VoucherResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.User.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
@Validated
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<PageResponse<VoucherResponse>> getVouchers(
            @ModelAttribute PageFilter filter,
            @ModelAttribute VoucherFilter voucherFilter) {

        Page<VoucherResponse> responses = voucherService.getVouchers(PageFilter.toPageAble(filter), voucherFilter);
        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @GetMapping("/customer")
    public ResponseEntity<PageResponse<VoucherResponse>> getVouchersForCustomer(@ModelAttribute PageFilter filter,
            @ModelAttribute VoucherFilter voucherFilter) {
        System.out.println("Fetching vouchers for customer with code: " + voucherFilter);
        Page<VoucherResponse> responses = voucherService.getVoucherForCustomer(PageFilter.toPageAble(filter),
                voucherFilter);
        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @PostMapping
    public ResponseEntity<VoucherResponse> createVoucher(@Valid @RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.createVoucher(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponse> updateVoucher(@PathVariable Long id, @Valid @RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.updateVoucher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }

}
