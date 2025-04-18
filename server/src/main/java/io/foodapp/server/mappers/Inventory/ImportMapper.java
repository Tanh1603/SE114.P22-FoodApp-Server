package io.foodapp.server.mappers.Inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportDetailResponse;
import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.repositories.Inventory.ImportDetailRepository;
import io.foodapp.server.repositories.Inventory.SupplierRepository;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ImportDetailMapper.class })
public interface ImportMapper {
    Import toEntity(ImportRequest dto,
            @Context SupplierRepository supplierRepository,
            @Context StaffRepository staffRepository,
            @Context ImportDetailRepository importDetailRepository,
            @Context ImportDetailMapper importDetailMapper,
            @Context IngredientRepository ingredientRepository);


    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.fullName", target = "staffName")
    ImportResponse toDTO(Import import1);

    List<Import> toEntities(List<ImportRequest> importRequests);
    List<ImportResponse> toDTOs(List<Import> imports);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ImportRequest dto, @MappingTarget Import entity,
            @Context SupplierRepository supplierRepository,
            @Context StaffRepository staffRepository,
            @Context ImportDetailRepository importDetailRepository,
            @Context ImportDetailMapper importDetailMapper,
            @Context IngredientRepository ingredientRepository);
    
    @AfterMapping
    default void setRelatedEntities(ImportRequest dto, @MappingTarget Import entity,
            @Context SupplierRepository supplierRepository,
            @Context StaffRepository staffRepository,
            @Context ImportDetailRepository importDetailRepository,
            @Context ImportDetailMapper importDetailMapper,
            @Context IngredientRepository ingredientRepository) {

        if (dto.getSupplierId() != null) {
            entity.setSupplier(supplierRepository
                    .findById(dto.getSupplierId()).orElseThrow(() -> new RuntimeException("Supplier not found")));
        } else {
            entity.setSupplier(null);
        }
        if (dto.getStaffId() != null) {
            entity.setStaff(staffRepository
                    .findById(dto.getStaffId()).orElseThrow(() -> new RuntimeException("Staff not found")));
        } else {
            entity.setStaff(null);
        }

        if (dto.getImportDetails() != null) {
            entity.setImportDetails(dto.getImportDetails().stream().map(item -> {
                ImportDetail upsert;
                if (item.getId() == null) {
                    upsert = importDetailMapper.toEntity(item, entity, ingredientRepository);
                } else {
                    upsert = importDetailRepository.findById(item.getId())
                            .orElseThrow(() -> new RuntimeException("Import detail not found for ID: " + item.getId()));
                    importDetailMapper.updateEntityFromDto(item, upsert, entity, ingredientRepository);
                }
                return upsert;
            }).collect(Collectors.toList()));
        }
    }

    @AfterMapping
    default void handleToResponse(@MappingTarget ImportResponse dto, Import entity) {
        if (dto.getImportDetails() != null) {
            List<ImportDetailResponse> filteredItems = dto.getImportDetails().stream()
                    .filter(item -> !item.isDeleted())
                    .toList();
            BigDecimal total = filteredItems.stream()
                .map(item -> item.getCost().multiply(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    

            dto.setTotalPrice(total);
            dto.setImportDetails(filteredItems);
        }
    }        
}
