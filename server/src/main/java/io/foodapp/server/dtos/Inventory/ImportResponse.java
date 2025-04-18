package io.foodapp.server.dtos.Inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime importDate;

    private BigDecimal totalPrice;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    @Builder.Default
    private List<ImportDetailResponse> importDetails = new ArrayList<>();
}
