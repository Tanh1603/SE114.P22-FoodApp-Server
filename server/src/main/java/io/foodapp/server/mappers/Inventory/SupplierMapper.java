package io.foodapp.server.mappers.Inventory;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.SupplierDTO;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.InventoryModel.Supplier;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper extends GenericMapper<Supplier, SupplierDTO> {

}