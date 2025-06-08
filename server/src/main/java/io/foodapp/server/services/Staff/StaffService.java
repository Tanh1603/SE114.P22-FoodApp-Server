package io.foodapp.server.services.Staff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.StaffFilter;
import io.foodapp.server.dtos.Specification.StaffSpecification;
import io.foodapp.server.dtos.Staff.StaffRequest;
import io.foodapp.server.dtos.Staff.StaffResponse;
import io.foodapp.server.mappers.Staff.StaffMapper;
import io.foodapp.server.models.StaffModel.SalaryHistory;
import io.foodapp.server.models.StaffModel.Staff;
import io.foodapp.server.repositories.Staff.SalaryHistoryRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import io.foodapp.server.services.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final SalaryHistoryRepository salaryHistoryRepository;
    private final CloudinaryService cloudinaryService;
    private final StaffMapper staffMapper;

    public Page<StaffResponse> getStaffs(StaffFilter filter, Pageable pageable) {
        try {
            Specification<Staff> specification = StaffSpecification.withFilter(filter);
            Page<Staff> staffPage = staffRepository.findAll(specification, pageable);
            return staffPage.map(staffMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching staff data: " + e.getMessage());
            throw new RuntimeException("Error fetching staff data: " + e.getMessage());
        }
    }

    public StaffResponse createStaff(StaffRequest request) {
        try {
            String avatar = null;
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                avatar = cloudinaryService.uploadFile(request.getAvatar());
            }
            Staff staff = staffMapper.toEntity(request);
            staff.setAvatar(avatar);
            staffRepository.save(staff);
            return staffMapper.toDTO(staffRepository.save(staff));

        } catch (Exception e) {
            throw new RuntimeException("Error creating staff: " + e.getMessage());
        }
    }

    public StaffResponse updateStaff(Long id, StaffRequest request) {
        String newImageUrl = null;
        try {
            Staff updateStaff = staffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
            if (updateStaff.getAvatar() != null && !updateStaff.getAvatar().isEmpty()) {
                cloudinaryService.deleteFile(updateStaff.getAvatar());
            }
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                newImageUrl = cloudinaryService.uploadFile(request.getAvatar());
            }
            updateStaff.setAvatar(newImageUrl);
            staffMapper.updateEntityFromDTO(request, updateStaff);
            return staffMapper.toDTO(staffRepository.save(updateStaff));

        } catch (Exception e) {
            System.out.println("Error updating staff: " + e.getMessage());
            if (newImageUrl != null) {
                try {
                    cloudinaryService.deleteFile(newImageUrl);
                } catch (Exception deleteException) {
                    // Log the error but do not throw it
                    throw new RuntimeException("Error deleting new image: " + deleteException.getMessage());
                }
            }
            throw new RuntimeException("Error updating staff: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteStaff(Long id) {
        try {
            staffRepository.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Error deleting staff: " + e.getMessage());
        }
    }

    public AtomicInteger calculateSalary() {
        try {
            List<Staff> staffs = staffRepository.findAll();
            if (staffs.isEmpty()) {
                throw new RuntimeException("No available staff found.");
            }

            LocalDate date = LocalDate.now();
            int month = date.getMonthValue();
            int year = date.getYear();
            AtomicInteger count = new AtomicInteger(0);

            staffs.forEach(staff -> {
                boolean alreadyExists = salaryHistoryRepository
                        .existsByStaffAndMonthAndYear(staff, month, year);
                if (!alreadyExists) {
                    SalaryHistory salaryHistory = SalaryHistory.builder()
                            .staff(staff)
                            .month(month)
                            .year(year)
                            .currentSalary(staff.getBasicSalary())
                            .build();
                    salaryHistoryRepository.save(salaryHistory);
                    count.incrementAndGet();
                }

            });

            return count;
        } catch (Exception e) {
            System.out.println("Error calculating salary: " + e.getMessage());
            throw new RuntimeException("Error calculating salary: " + e.getMessage());
        }
    }

    public BigDecimal getTotalSalaryByMonthAndYear(int month, int year) {
        try {
            BigDecimal sum = salaryHistoryRepository.getTotalSalaryByMonthAndYear(month, year);
            return sum != null ? sum : BigDecimal.ZERO;
        } catch (Exception e) {
            System.out.println("Error fetching total salary: " + e.getMessage());
            throw new RuntimeException("Error fetching total salary: " + e.getMessage());
        }
    }

}
