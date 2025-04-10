package io.foodapp.server.mappers.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.InventoryModel.Import;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportRequestMapper extends GenericMapper<Import, ImportRequest> {
    @Override
    @Mapping(target = "importDetails", ignore = true)
    Import toEntity(ImportRequest dto);
}
