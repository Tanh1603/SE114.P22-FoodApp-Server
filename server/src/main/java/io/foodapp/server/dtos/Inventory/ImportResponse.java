package io.foodapp.server.dtos.Inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.foodapp.server.dtos.Staff.StaffDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResponse {
    private Long id;
    
    private SupplierDTO supplier;

    private StaffDTO staff;

    private LocalDateTime importDate;

    private List<ImportDetailResponse> importDetails = new ArrayList<>();
}
