package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.RecipeDetailRequest;
import io.foodapp.server.dtos.Menu.RecipeDetailResponse;
import io.foodapp.server.models.MenuModel.Recipe;
import io.foodapp.server.models.MenuModel.RecipeDetail;
import io.foodapp.server.repositories.Menu.IngredientRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeDetailMapper {
    RecipeDetail toEntity(RecipeDetailRequest dto,
            @Context Recipe recipe,
            @Context IngredientRepository ingredientRepository);

    @Mapping(target = "deleted", source = "deleted")
        RecipeDetailResponse toDTO(RecipeDetail entity);

    List<RecipeDetail> toEntities(List<RecipeDetailRequest> dtos);

    List<RecipeDetailResponse> toDTOs(List<RecipeDetail> entities);

    @AfterMapping
    default void setRelatedEntities(RecipeDetailRequest dto, @MappingTarget RecipeDetail entity,
            @Context Recipe recipe,
            @Context IngredientRepository ingredientRepository) {

        if (dto.getIngredientId() == null) {
            throw new RuntimeException("Ingredient ID is required");
        }
        entity.setRecipe(recipe);
        entity.setIngredient(ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found for ID: " + dto.getIngredientId())));
    }

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(RecipeDetailRequest dto, @MappingTarget RecipeDetail entity,
            @Context Recipe recipe,
            @Context IngredientRepository ingredientRepository);

}
