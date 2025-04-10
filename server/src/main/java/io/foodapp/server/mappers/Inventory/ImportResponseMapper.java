package io.foodapp.server.mappers.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.models.InventoryModel.Import;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportResponseMapper {
    ImportResponse toDTO(Import entity);

}
