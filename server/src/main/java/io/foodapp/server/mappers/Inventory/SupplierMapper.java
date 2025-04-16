package io.foodapp.server.mappers.Inventory;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.SupplierRequest;
import io.foodapp.server.dtos.Inventory.SupplierResponse;
import io.foodapp.server.models.InventoryModel.Supplier;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {
    Supplier toEntity(SupplierRequest supplierRequest);
    SupplierResponse toDTO(Supplier supplier);

    List<Supplier> toEntities(List<SupplierRequest> supplierRequests);
    List<SupplierResponse> toDTOs(List<Supplier> suppliers);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(SupplierRequest supplierRequest, @MappingTarget Supplier supplier);
}