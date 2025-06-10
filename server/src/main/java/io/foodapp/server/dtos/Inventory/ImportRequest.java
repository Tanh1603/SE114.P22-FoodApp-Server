package io.foodapp.server.dtos.Inventory;

import java.time.LocalDate;
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

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Import date is required")
    private LocalDate importDate;

    @NotNull
    @Builder.Default
    private List<ImportDetailRequest> importDetails = new ArrayList<>();
}
