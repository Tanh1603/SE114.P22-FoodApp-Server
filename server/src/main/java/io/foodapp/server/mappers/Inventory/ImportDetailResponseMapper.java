package io.foodapp.server.mappers.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportDetailResponse;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.InventoryModel.ImportDetail;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportDetailResponseMapper extends GenericMapper<ImportDetail, ImportDetailResponse> {
    // Custom mapping methods can be added here if needed
}
