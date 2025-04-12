package io.foodapp.server.mappers.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.InventoryModel.Import;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportResponseMapper extends GenericMapper<Import, ImportResponse> {

}
