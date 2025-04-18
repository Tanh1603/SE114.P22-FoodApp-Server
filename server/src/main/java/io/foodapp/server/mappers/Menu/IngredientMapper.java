package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.dtos.Menu.IngredientResponse;
import io.foodapp.server.models.MenuModel.Ingredient;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IngredientMapper{
    Ingredient toEntity(IngredientRequest ingredientRequest);

    @Mapping(target = "unit", source = "unit.name")
    IngredientResponse toDTO(Ingredient ingredient);

    List<Ingredient> toEntities(List<IngredientRequest> ingredientRequests);
    List<IngredientResponse> toDTOs(List<Ingredient> ingredients);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(IngredientRequest ingredientRequest, @MappingTarget Ingredient ingredient);
}
