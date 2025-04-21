package io.foodapp.server.services.Menu;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.MenuItemFilter;
import io.foodapp.server.dtos.Menu.MenuItemRequest;
import io.foodapp.server.dtos.Menu.MenuItemResponse;
import io.foodapp.server.dtos.Specification.MenuItemSpecification;
import io.foodapp.server.mappers.Menu.MenuItemMapper;
import io.foodapp.server.models.MenuModel.MenuItem;
import io.foodapp.server.repositories.Menu.MenuItemRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.services.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    private final MenuItemMapper menuItemMapper;
    private final CloudinaryService cloudinaryService;

    public Page<MenuItemResponse> getMenuItems(MenuItemFilter menuItemFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<MenuItem> specification = MenuItemSpecification.withFilter(menuItemFilter);
            Page<MenuItem> menus = menuItemRepository.findAll(specification, pageable);
            return menus.map(menuItemMapper::toDTO);
        } catch (RuntimeException e) {
            System.out.println("Error fetching MenuItems: " + e.getMessage());
            throw new RuntimeException("Error fetching MenuItems", e);
        }
    }


    public MenuItemResponse getMenuItemDetailById(Long id) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("MenuItem not found"));
            return menuItemMapper.toDTO(menuItem);
        } catch (RuntimeException e) {
            System.out.println("Error fetching MenuItem detail: " + e.getMessage());
            throw new RuntimeException("Error fetching MenuItem detail", e);
        }
    }

     public MenuItemResponse createMenuItem(MenuItemRequest request) {
        try {
            String imgUrl = null;
            if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
                imgUrl = cloudinaryService.uploadFile(request.getImageUrl());
            }
            
            MenuItem menuItem = menuItemMapper.toEntity(request, menuRepository);
            menuItem.setImageUrl(imgUrl);

            return menuItemMapper.toDTO(menuItemRepository.save(menuItem));

        } catch (IOException e) {
            throw new RuntimeException("Error creating staff: " + e.getMessage());
        }
    }

    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        String newImageUrl = null;
        try {
            MenuItem updateMenuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
            
            if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
                if (updateMenuItem.getImageUrl() != null && !updateMenuItem.getImageUrl().isEmpty()) {
                    cloudinaryService.deleteFile(updateMenuItem.getImageUrl());
                }
                newImageUrl = cloudinaryService.uploadFile(request.getImageUrl());
            }
            
            updateMenuItem.setImageUrl(newImageUrl);
            menuItemMapper.updateEntityFromDto(request, updateMenuItem, menuRepository);
            return menuItemMapper.toDTO(menuItemRepository.save(updateMenuItem));

        } catch (Exception e) {
            System.out.println("Error updating staff: " + e.getMessage());
            if (newImageUrl != null) {
                try {
                    cloudinaryService.deleteFile(newImageUrl);
                } catch (Exception deleteException) {
                    // Log the error but do not throw it
                    throw new RuntimeException("Error deleting new image: " + deleteException.getMessage());
                }
            }
            throw new RuntimeException("Error updating staff: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found"));
            menuItemRepository.delete(menuItem);
        } catch (RuntimeException e) {
            System.out.println("Error deleting import: " + e.getMessage());
            throw new RuntimeException("Error deleting import", e);
        }
    }

    public void setMenuItemActive(Long id, boolean isActive) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found"));
            menuItem.setActive(isActive);
            menuItemRepository.save(menuItem);
        } catch (RuntimeException e) {
            System.out.println("Error updating menu item status: " + e.getMessage());
            throw new RuntimeException("Error updating menu item status", e);
        }
    }
}
