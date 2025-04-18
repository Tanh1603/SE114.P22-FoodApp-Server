package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.models.MenuModel.Menu;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    Menu toEntity(MenuRequest menuRequest);
    
    MenuResponse toDTO(Menu menu);

    List<Menu> toEntities(List<MenuRequest> menuRequests);
    List<MenuResponse> toDTOs(List<Menu> menus);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(MenuRequest recipeDetailRequest, @MappingTarget Menu menu);
}
