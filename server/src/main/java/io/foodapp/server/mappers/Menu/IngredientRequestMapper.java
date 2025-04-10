package io.foodapp.server.mappers.Menu;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.IngredientRequest;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.MenuModel.Ingredient;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IngredientRequestMapper extends GenericMapper<Ingredient, IngredientRequest>{

}
