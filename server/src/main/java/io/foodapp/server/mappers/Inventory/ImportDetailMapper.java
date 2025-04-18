package io.foodapp.server.mappers.Inventory;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Inventory.ImportDetailRequest;
import io.foodapp.server.dtos.Inventory.ImportDetailResponse;
import io.foodapp.server.mappers.Menu.IngredientMapper;
import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.repositories.Menu.IngredientRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {IngredientMapper.class })
public interface ImportDetailMapper {

    ImportDetail toEntity(ImportDetailRequest dto,
            @Context Import import1,
            @Context IngredientRepository ingredientRepository);

    @Mapping(target = "isDeleted", source = "deleted")
    ImportDetailResponse toDTO(ImportDetail importDetail);

    List<ImportDetail> toEntities(List<ImportDetailRequest> dtos);

    List<ImportDetailResponse> toDTOs(List<ImportDetail> entities);

    @AfterMapping
    default void setRelatedEntities(ImportDetailRequest dto, @MappingTarget ImportDetail entity,
            @Context Import import1,
            @Context IngredientRepository ingredientRepository) {

        if (dto.getIngredientId() == null) {
            throw new RuntimeException("Ingredient ID is required");
        }
        entity.setAnImport(import1);
        entity.setIngredient(ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found for ID: " + dto.getIngredientId())));
    }

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ImportDetailRequest dto, @MappingTarget ImportDetail entity,
            @Context Import import1,
            @Context IngredientRepository ingredientRepository);

}
