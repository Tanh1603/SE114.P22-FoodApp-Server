package io.foodapp.server.mappers.Inventory;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.InventoryResponse;
import io.foodapp.server.models.InventoryModel.Inventory;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {

    @Mapping(target = "ingredientName", source = "ingredient.name")
    @Mapping(target = "unit", source = "ingredient.unit.name")
    InventoryResponse toDTO(Inventory inventory);

    List<InventoryResponse> toDTOs(List<Inventory> inventories);
}
