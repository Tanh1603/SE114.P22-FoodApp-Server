package io.foodapp.server.dtos.Staff;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.foodapp.server.utils.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {
    private Long id;
    private String fullName;
    private String position;
    private String phone;
    private String gender;
    private String address;

    private ImageInfo avatar;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private double basicSalary;

    private List<SalaryHistoryResponse> salaryHistories;
}
