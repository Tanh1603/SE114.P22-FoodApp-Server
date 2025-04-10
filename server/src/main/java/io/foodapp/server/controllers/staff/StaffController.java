package io.foodapp.server.controllers.staff;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Staff.StaffDTO;
import io.foodapp.server.services.Staff.StaffService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // Endpoint to get all available staff
    @GetMapping("/available")
    public ResponseEntity<List<StaffDTO>> getAvailableStaff() {
        List<StaffDTO> availableStaff = staffService.getAvailableStaff();
        return ResponseEntity.ok(availableStaff);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<StaffDTO>> getDeletedStaff() {
        List<StaffDTO> deletedStaff = staffService.getDeletedStaff();
        return ResponseEntity.ok(deletedStaff);
    }

    @PostMapping
    public ResponseEntity<StaffDTO> createStaff(@RequestBody StaffDTO staffDTO) {
        StaffDTO createdStaff = staffService.createStaff(staffDTO);
        return ResponseEntity.ok(createdStaff);
    }

    @PutMapping
    public ResponseEntity<StaffDTO> updateStaff(@RequestBody StaffDTO staffDTO) {
        StaffDTO updatedStaff = staffService.updateStaff(staffDTO);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/caculate-salary")
    public ResponseEntity<Map<String, Integer>> calculateSalary() {
        AtomicInteger count = staffService.caculateSalary();
        return ResponseEntity.ok(Map.of("number records", count.get()));
    }

    @GetMapping("/total-salary")
    public ResponseEntity<Map<String, Double>> getTotalSalary(@RequestParam int month, @RequestParam int year) {
        Double totalSalary = staffService.getTotalSalaryByMonthAndYear(month, year);
        return ResponseEntity.ok(Map.of("totalSalary", totalSalary));
    }

}
