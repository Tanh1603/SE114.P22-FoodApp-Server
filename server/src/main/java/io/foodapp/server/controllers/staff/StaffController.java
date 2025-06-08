package io.foodapp.server.controllers.staff;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

import io.foodapp.server.dtos.Filter.StaffFilter;
import io.foodapp.server.dtos.Staff.StaffRequest;
import io.foodapp.server.dtos.Staff.StaffResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Staff.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // Endpoint to get all available staff
    @GetMapping
    public ResponseEntity<PageResponse<StaffResponse>> getAllStaffs(
            @ModelAttribute StaffFilter filter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StaffResponse> staffs = staffService.getStaffs(filter, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(staffs));
    }

    @PostMapping(consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffResponse> createStaff(@Valid @ModelAttribute StaffRequest request) {
        StaffResponse createdStaff = staffService.createStaff(request);
        return ResponseEntity.ok(createdStaff);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffResponse> updateStaff(
            @Valid @ModelAttribute StaffRequest request,
            @PathVariable Long id) {
        StaffResponse updateStaff = staffService.updateStaff(id, request);
        return ResponseEntity.ok(updateStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate-salary")
    public ResponseEntity<Map<String, Integer>> calculateSalary() {
        AtomicInteger count = staffService.calculateSalary();
        return ResponseEntity.ok(Map.of("number records", count.get()));
    }

    // @GetMapping("/total-salary")
    // public ResponseEntity<Map<String, Double>> getTotalSalary(@RequestParam int month, @RequestParam int year) {
    //     Double totalSalary = staffService.getTotalSalaryByMonthAndYear(month, year);
    //     return ResponseEntity.ok(Map.of("totalSalary", totalSalary));
    // }

}
