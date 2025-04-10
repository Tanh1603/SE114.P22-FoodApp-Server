package io.foodapp.server.dtos.Inventory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long id;
    
    @NotNull
    private Long supplierId;

    @NotNull
    private Long staffId;

    @NotNull
    private LocalDateTime importDate;

    @NotNull
    @Builder.Default
    private List<ImportDetailRequest> importDetails = new ArrayList<>();
}
