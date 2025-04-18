package io.foodapp.server.mappers.Menu;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.MenuItemDetailResponse;
import io.foodapp.server.dtos.Menu.MenuItemRequest;
import io.foodapp.server.dtos.Menu.MenuItemResponse;
import io.foodapp.server.dtos.Menu.RecipeDetailResponse;
import io.foodapp.server.models.MenuModel.MenuItem;
import io.foodapp.server.models.MenuModel.RecipeDetail;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.repositories.Menu.RecipeDetailRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RecipeDetailMapper.class })
public interface MenuItemMapper {
    MenuItem toEntity(MenuItemRequest dto,
            @Context MenuRepository menuRepository,
            @Context RecipeDetailRepository recipeDetailRepository,
            @Context RecipeDetailMapper recipeDetailMapper,
            @Context IngredientRepository ingredientRepository);


    @Mapping(source = "menu.name", target = "menuName")
    MenuItemResponse toDTO(MenuItem menuItem);

    @Mapping(source = "menu.name", target = "menuName")
    MenuItemDetailResponse toDetailDTO(MenuItem menuItem);

    List<MenuItem> toEntities(List<MenuItemRequest> menuItemRequests);
    List<MenuItemResponse> toDTOs(List<MenuItem> menuItems);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(MenuItemRequest dto, @MappingTarget MenuItem entity,
            @Context MenuRepository menuRepository,
            @Context RecipeDetailRepository recipeDetailRepository,
            @Context RecipeDetailMapper recipeDetailMapper,
            @Context IngredientRepository ingredientRepository);
    
    @AfterMapping
    default void setRelatedEntities(MenuItemRequest dto, @MappingTarget MenuItem entity,
            @Context MenuRepository menuRepository,
            @Context RecipeDetailRepository recipeDetailRepository,
            @Context RecipeDetailMapper recipeDetailMapper,
            @Context IngredientRepository ingredientRepository) {

        if (dto.getMenuId() != null) {
            entity.setMenu(menuRepository
                    .findById(dto.getMenuId()).orElseThrow(() -> new RuntimeException("Menu not found")));
        } else {
            entity.setMenu(null);
        }

        if (dto.getRecipeDetails() != null) {
            entity.setRecipeDetails(dto.getRecipeDetails().stream().map(item -> {
                RecipeDetail upsert;
                if (item.getId() == null) {
                    upsert = recipeDetailMapper.toEntity(item, entity, ingredientRepository);
                } else {
                    upsert = recipeDetailRepository.findById(item.getId())
                            .orElseThrow(() -> new RuntimeException("Import detail not found for ID: " + item.getId()));
                    recipeDetailMapper.updateEntityFromDto(item, upsert, entity, ingredientRepository);
                }
                return upsert;
            }).collect(Collectors.toList()));
        }
    }

    @AfterMapping
    default void handleToResponse(@MappingTarget MenuItemDetailResponse dto, MenuItem entity) {
        if (dto.getRecipeDetails() != null) {
            List<RecipeDetailResponse> filteredItems = dto.getRecipeDetails().stream()
                    .filter(item -> !item.isDeleted())
                    .toList();
    
            dto.setRecipeDetails(filteredItems);
        }
    }        
}
