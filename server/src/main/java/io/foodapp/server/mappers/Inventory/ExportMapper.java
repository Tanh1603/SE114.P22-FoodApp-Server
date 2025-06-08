package io.foodapp.server.mappers.Inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ExportRequest;
import io.foodapp.server.dtos.Inventory.ExportResponse;
import io.foodapp.server.models.InventoryModel.Export;
import io.foodapp.server.models.InventoryModel.ExportDetail;
import io.foodapp.server.repositories.Inventory.ExportDetailRepository;
import io.foodapp.server.repositories.Inventory.InventoryRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        ExportDetailMapper.class })
public interface ExportMapper {
    Export toEntity(ExportRequest dto,
            @Context StaffRepository staffRepository,
            @Context ExportDetailRepository exportDetailRepository,
            @Context ExportDetailMapper exportDetailMapper,
            @Context InventoryRepository inventoryRepository);

    ExportResponse toDTO(Export export);

    List<Export> toEntities(List<ExportRequest> exportRequests);

    List<ExportResponse> toDTOs(List<Export> exports);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ExportRequest dto, @MappingTarget Export entity,
            @Context StaffRepository staffRepository,
            @Context ExportDetailRepository exportDetailRepository,
            @Context ExportDetailMapper exportDetailMapper,
            @Context InventoryRepository inventoryRepository);

    @AfterMapping
    default void setRelatedEntities(ExportRequest dto, @MappingTarget Export entity,
            @Context StaffRepository staffRepository,
            @Context ExportDetailRepository exportDetailRepository,
            @Context ExportDetailMapper exportDetailMapper,
            @Context InventoryRepository inventoryRepository) {

        if (dto.getExportDetails() != null) {
            entity.setExportDetails(dto.getExportDetails().stream().map(item -> {
                ExportDetail upsert;
                if (item.getId() == null) {
                    upsert = exportDetailMapper.toEntity(item, entity, inventoryRepository);
                } else {
                    upsert = exportDetailRepository.findById(item.getId())
                            .orElseThrow(() -> new RuntimeException("Export detail not found for ID: " + item.getId()));
                    exportDetailMapper.updateEntityFromDto(item, upsert, entity, inventoryRepository);
                }
                return upsert;
            }).collect(Collectors.toList()));
        }
    }
}
