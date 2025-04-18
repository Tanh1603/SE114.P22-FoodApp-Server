package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.MenuItemRequest;
import io.foodapp.server.dtos.Menu.MenuItemResponse;
import io.foodapp.server.models.MenuModel.MenuItem;
import io.foodapp.server.repositories.Menu.MenuRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper {

    @Mapping(target = "imageUrl", ignore = true)
    MenuItem toEntity(MenuItemRequest dto,
            @Context MenuRepository menuRepository);

    @Mapping(source = "menu.name", target = "menuName")
    MenuItemResponse toDTO(MenuItem menuItem);

    List<MenuItemResponse> toDTOs(List<MenuItem> menuItems);

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "imageUrl", ignore = true)
    void updateEntityFromDto(MenuItemRequest dto, @MappingTarget MenuItem entity,
            @Context MenuRepository menuRepository);

    @AfterMapping
    default void setRelatedEntities(MenuItemRequest dto, @MappingTarget MenuItem entity,
            @Context MenuRepository menuRepository) {

        if (dto.getMenuId() != null) {
            entity.setMenu(menuRepository
                    .findById(dto.getMenuId()).orElseThrow(() -> new RuntimeException("Menu not found")));
        } else {
            entity.setMenu(null);
        }
        entity.setAvailable(true);
    }
}
