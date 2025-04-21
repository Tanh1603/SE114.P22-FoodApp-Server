package io.foodapp.server.services.Menu;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.mappers.Menu.MenuMapper;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.repositories.Menu.MenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public List<MenuResponse> getActiveMenus() {
        try {
            return menuMapper.toDTOs(menuRepository.findByIsActiveTrue());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching menus", e);
        }
    }

    public List<MenuResponse> getInActiveMenus() {
        try {
            return menuMapper.toDTOs(menuRepository.findByIsActiveFalse());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching menus", e);
        }
    }

    public MenuResponse createMenu(MenuRequest request) {
        try {
            Menu existingMenu = menuRepository.findByName(request.getName());
            if (existingMenu != null) {
                if (!existingMenu.isActive()) {
                    existingMenu.setActive(true);
                    return menuMapper.toDTO(menuRepository.save(existingMenu));
                } else {
                    throw new IllegalArgumentException("Menu already exists.");
                }
            }
            Menu newMenu = menuMapper.toEntity(request);
            newMenu.setActive(true);
            return menuMapper.toDTO(menuRepository.save(newMenu));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating menu", e);
        }
    }


    public MenuResponse updateMenu(Long id, MenuRequest request) {
        try {
            Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
            menuMapper.updateEntityFromDto(request, existingMenu);
            
            return menuMapper.toDTO(menuRepository.save(existingMenu));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating menu", e);
        }
    }

    public void setMenuActive(Long id, boolean isActive) {
        try {
            Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
    
            menu.setActive(isActive);
    
            if (!isActive) {
                // Nếu tắt Menu thì tắt luôn các MenuItem của nó
                if (menu.getMenuItems() != null) {
                    menu.getMenuItems().forEach(item -> item.setActive(false));
                }
            }
    
            menuRepository.save(menu);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating menu status", e);
        }
    }
    

    public void deleteMenu(Long id) {
        try {
            Menu menu =  menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
            
            if(menu.getMenuItems() != null && !menu.getMenuItems().isEmpty()) {
                throw new RuntimeException("Menu cannot be deleted because it has menu items associated with it.");
            }

            menuRepository.delete(menu);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting menu", e);
        }
    }
}
