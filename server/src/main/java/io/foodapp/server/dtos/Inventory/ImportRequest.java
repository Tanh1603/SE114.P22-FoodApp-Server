package io.foodapp.server.dtos.Inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportRequest {    
    @NotNull(message = "SupplierId is required")
    private Long supplierId;

    @NotNull(message = "StaffId is required")
    private Long staffId;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "Import date is required")
    private LocalDateTime importDate;

    @NotNull
    @Builder.Default
    private List<ImportDetailRequest> importDetails = new ArrayList<>();
}
