package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.MenuItemRequest;
import io.foodapp.server.dtos.Menu.MenuItemResponse;
import io.foodapp.server.models.MenuModel.MenuItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper {
    MenuItem toEntity(MenuItemRequest menuItemRequest);
    
    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.name", target = "recipeName")
    MenuItemResponse toDTO(MenuItem menuItem);

    List<MenuItem> toEntities(List<MenuItemRequest> menuItemRequests);
    List<MenuItemResponse> toDTOs(List<MenuItem> menuItems);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(MenuItemRequest menuItemRequest, @MappingTarget MenuItem menuItem);
}
