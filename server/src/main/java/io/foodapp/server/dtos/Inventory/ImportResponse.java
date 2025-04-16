package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    private Long supplierId;
    private String supplierName;
    private Long staffId;
    private String staffName;
    private LocalDateTime importDate;

    private BigDecimal totalPrice;

    private boolean isDeleted;

    @Builder.Default
    private List<ImportDetailResponse> importDetails = new ArrayList<>();
}
