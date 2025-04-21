package io.foodapp.server.mappers.Inventory;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ExportDetailRequest;
import io.foodapp.server.dtos.Inventory.ExportDetailResponse;
import io.foodapp.server.models.InventoryModel.Export;
import io.foodapp.server.models.InventoryModel.ExportDetail;
import io.foodapp.server.repositories.Inventory.InventoryRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExportDetailMapper {

    ExportDetail toEntity(ExportDetailRequest dto,
            @Context Export export,
            @Context InventoryRepository inventoryRepository);

    @Mapping(target = "inventoryId", source = "inventory.id")
    @Mapping(target = "ingredientName", source = "inventory.ingredient.name")
    @Mapping(target = "expiryDate", source = "inventory.expiryDate")
    @Mapping(target = "cost", source = "inventory.cost")
    ExportDetailResponse toDTO(ExportDetail exportDetail);

    List<ExportDetail> toEntities(List<ExportDetailRequest> dtos);

    List<ExportDetailResponse> toDTOs(List<ExportDetail> entities);

    @AfterMapping
    default void setRelatedEntities(ExportDetailRequest dto, @MappingTarget ExportDetail entity,
            @Context Export export,
            @Context InventoryRepository inventoryRepository) {

        entity.setInventory(inventoryRepository.findById(dto.getInventoryId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for ID: " + dto.getInventoryId())));
        entity.setExport(export);
        
    }

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ExportDetailRequest dto, @MappingTarget ExportDetail entity,
            @Context Export export,
            @Context InventoryRepository inventoryRepository);

}
