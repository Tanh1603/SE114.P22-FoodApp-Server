package io.foodapp.server.services.Menu;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.mappers.Menu.MenuMapper;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.repositories.Menu.MenuItemRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final MenuItemRepository menuItemRepository;

    public List<MenuResponse> getMenusAvailable() {
        try {
            return menuMapper.toDTOs(menuRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching menus", e);
        }
    }

    public List<MenuResponse> getMenusDeleted() {
        try {
            return menuMapper.toDTOs(menuRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching menus", e);
        }
    }

    public MenuResponse createMenu(MenuRequest request) {
        try {
            return menuMapper.toDTO(menuRepository.save(menuMapper.toEntity(request)));
        } catch (Exception e) {
            throw new RuntimeException("Error creating menu", e);
        }
    }


    public MenuResponse updateMenu(Long id, MenuRequest request) {
        try {
            Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
            menuMapper.updateEntityFromDto(request, existingMenu);
            return menuMapper.toDTO(menuRepository.save(existingMenu));
        } catch (Exception e) {
            throw new RuntimeException("Error updating menu", e);
        }
    }

    public void deleteMenu(Long id) {
        try {
            Menu menu =  menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
            
            menu.getMenuItems().forEach(menuItem -> {
                menuItem.setDeleted(true);
                menuItemRepository.save(menuItem);
            });
            
            menu.setDeleted(true);
            menuRepository.save(menu);

        } catch (Exception e) {
            throw new RuntimeException("Error deleting menu", e);
        }
    }
}
