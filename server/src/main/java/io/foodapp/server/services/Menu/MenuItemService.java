package io.foodapp.server.services.Menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.foodapp.server.dtos.Filter.MenuItemFilter;
import io.foodapp.server.dtos.Menu.MenuItemDetailResponse;
import io.foodapp.server.dtos.Menu.MenuItemRequest;
import io.foodapp.server.dtos.Menu.MenuItemResponse;
import io.foodapp.server.dtos.Specification.MenuItemSpecification;
import io.foodapp.server.mappers.Menu.MenuItemMapper;
import io.foodapp.server.mappers.Menu.RecipeDetailMapper;
import io.foodapp.server.models.MenuModel.MenuItem;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Menu.MenuItemRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.repositories.Menu.RecipeDetailRepository;
import io.foodapp.server.services.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RecipeDetailRepository recipeDetailRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuRepository menuRepository;

    private final MenuItemMapper menuItemMapper;
    private final RecipeDetailMapper recipeDetailMapper;
    private final CloudinaryService cloudinaryService;

    public Page<MenuItemResponse> getMenuItems(MenuItemFilter menuItemFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<MenuItem> specification = MenuItemSpecification.withFilter(menuItemFilter);
            Page<MenuItem> menus = menuItemRepository.findAll(specification, pageable);
            return menus.map(menuItemMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching MenuItems: " + e.getMessage());
            throw new RuntimeException("Error fetching MenuItems", e);
        }
    }


    public MenuItemDetailResponse getMenuItemDetailById(Long id) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("MenuItem not found"));
            return menuItemMapper.toDetailDTO(menuItem);
        } catch (Exception e) {
            System.out.println("Error fetching MenuItem detail: " + e.getMessage());
            throw new RuntimeException("Error fetching MenuItem detail", e);
        }
    }

    @Transactional
public MenuItemResponse createMenuItem(MenuItemRequest request, MultipartFile imgFile) {
    try {
        if (imgFile != null && !imgFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imgFile);
            request.setImageUrl(imageUrl);
        }
        MenuItem menuItem = menuItemMapper.toEntity(
                request,
                menuRepository,
                recipeDetailRepository,
                recipeDetailMapper,
                ingredientRepository);

        // Nếu không có recipe thì đánh dấu là không sẵn sàng
        if (menuItem.getRecipeDetails() == null || menuItem.getRecipeDetails().isEmpty()) {
            menuItem.setAvailable(false);
        }

        return menuItemMapper.toDTO(menuItemRepository.saveAndFlush(menuItem));
    } catch (Exception e) {
        throw new RuntimeException("Error creating MenuItem: " + e.getMessage());
    }
}


    @Transactional
    public MenuItemResponse updateImport(Long id, MenuItemRequest importRequest) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("MenuItem not found"));
            // Cập nhật các trường cơ bản
            menuItemMapper.updateEntityFromDto(importRequest, menuItem,
                    menuRepository,
                    recipeDetailRepository,
                    recipeDetailMapper,
                    ingredientRepository);
            return menuItemMapper.toDTO(menuItemRepository.saveAndFlush(menuItem));
        } catch (Exception e) {
            System.out.println("Error updating MenuItem: " + e.getLocalizedMessage());
            throw new RuntimeException("Error updating MenuItem: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteImport(Long id) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found"));
            menuItem.getRecipeDetails().forEach(importDetail -> {
                importDetail.setDeleted(true);
                recipeDetailRepository.save(importDetail);
            });
            menuItem.setDeleted(true);
            menuItemRepository.save(menuItem);
        } catch (Exception e) {
            System.out.println("Error deleting import: " + e.getMessage());
            throw new RuntimeException("Error deleting import", e);
        }
    }
    
}
