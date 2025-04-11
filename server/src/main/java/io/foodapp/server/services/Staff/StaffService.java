package io.foodapp.server.services.Staff;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.foodapp.server.dtos.Staff.StaffDTO;
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

    public List<StaffDTO> getAvailableStaff() {
        try {
            return staffMapper.toDtoList(staffRepository.findByIsDeletedFalse());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching staff data: " + e.getMessage());
        }
    }

    public List<StaffDTO> getDeletedStaff() {
        try {
            return staffMapper.toDtoList(staffRepository.findByIsDeletedTrue());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching deleted staff data: " + e.getMessage());
        }
    }

    public StaffDTO createStaff(StaffDTO staffDTO, MultipartFile avatar) {
        try {
            if (avatar != null && !avatar.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(avatar);
                staffDTO.setImageUrl(imageUrl);
            }
            return staffMapper.toDTO(staffRepository.save(staffMapper.toEntity(staffDTO)));

        } catch (Exception e) {
            throw new RuntimeException("Error creating staff: " + e.getMessage());
        }
    }

    public StaffDTO updateStaff(StaffDTO staffDTO, MultipartFile avatar) {
        String newImageUrl = null;
        try {
            if (avatar != null && !avatar.isEmpty()) {
                // Delete the old image if it exists
                if (staffDTO.getImageUrl() != null) {
                    cloudinaryService.deleteFile(staffDTO.getImageUrl());
                }
                // Upload the new image
                newImageUrl = cloudinaryService.uploadFile(avatar);
                staffDTO.setImageUrl(newImageUrl);
            }
            var existingStaff = staffRepository.findById(staffDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffDTO.getId()));

            staffMapper.updateEntityFromDto(staffDTO, existingStaff);
            return staffMapper.toDTO(staffRepository.save(existingStaff));

        } catch (Exception e) {
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
    public boolean deleteStaff(Long id) {
        try {
            var existingStaff = staffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

            existingStaff.setDeleted(true);
            if (existingStaff.getSalaryHistories() != null) {
                existingStaff.getSalaryHistories().forEach(salaryHistory -> salaryHistory.setDeleted(true));
            }
            staffRepository.save(existingStaff);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error deleting staff: " + e.getMessage());
        }
    }

    public AtomicInteger caculateSalary() {
        try {
            List<Staff> staffs = staffRepository.findByIsDeletedFalse();
            if (staffs.isEmpty()) {
                throw new RuntimeException("No available staff found.");
            }

            LocalDate date = LocalDate.now();
            int month = date.getMonthValue();
            int year = date.getYear();
            AtomicInteger count = new AtomicInteger(0);

            staffs.forEach(staff -> {

                boolean alreadyExists = salaryHistoryRepository
                        .existsByStaffAndMonthAndYearAndIsDeletedFalse(staff, month, year);
                if (!alreadyExists) {
                    SalaryHistory salaryHistory = SalaryHistory.builder()
                            .staff(staff)
                            .month(month)
                            .year(year)
                            .currentSalary(staff.getBasicSalary())
                            .isDeleted(false)
                            .build();
                    salaryHistoryRepository.save(salaryHistory);
                    count.incrementAndGet();
                }

            });

            return count;
        } catch (Exception e) {
            throw new RuntimeException("Error calculating salary: " + e.getMessage());
        }
    }

    public Double getTotalSalaryByMonthAndYear(int month, int year) {
        try {
            Double sum = salaryHistoryRepository.getTotalSalaryByMonthAndYear(month, year);
            return sum != null ? sum : 0.0;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching total salary: " + e.getMessage());
        }
    }

}
