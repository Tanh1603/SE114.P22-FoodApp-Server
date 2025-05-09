package io.foodapp.server.services.Menu;

import io.foodapp.server.dtos.Menu.FoodRequest;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.mappers.Menu.FoodMapper;
import io.foodapp.server.models.MenuModel.FavoriteFood;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.repositories.Menu.FavoriteFoodRepository;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.services.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final FoodRepository foodRepository;
    private final MenuRepository menuRepository;
    private final FoodMapper foodMapper;
    private final FavoriteFoodRepository favoriteFoodRepository;
    private final CloudinaryService cloudinaryService;

    public Page<MenuResponse> getMenus(Pageable pageable) {
        try {
            Page<Menu> menus = menuRepository.findAll(pageable);

            return menus.map(menu -> MenuResponse.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .active(menu.isActive())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Fetching menus failed: " + e.getMessage());
        }
    }

    public MenuResponse createMenu(MenuRequest request) {
        try {
            Menu newMenu = menuRepository.saveAndFlush(Menu.builder().name(request.getName()).build());
            return MenuResponse.builder().name(newMenu.getName()).id(newMenu.getId()).active(newMenu.isActive()).build();
        } catch (Exception e) {
            throw new RuntimeException("Creating menu failed: " + e.getMessage());
        }
    }

    public MenuResponse updateMenu(Integer id, MenuRequest request) {
        try {
            Menu update = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found for id: " + id));
            update.setName(request.getName());
            return MenuResponse.builder().name(update.getName()).id(update.getId()).active(update.isActive()).build();
        } catch (Exception e) {
            throw new RuntimeException("Updating menu failed: " + e.getMessage());
        }
    }

    public void updateMenuActive(Integer id, boolean active) {
        try {
            Menu update = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found for id: " + id));
            update.setActive(active);
            update.getFoods().forEach(food -> food.setActive(active));
            menuRepository.saveAndFlush(update);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu failed: " + e.getMessage());
        }
    }

    // Menu item
    public Page<FoodResponse> getFoodsByMenuId(Integer menuId, Pageable pageable) {
        try {
            Page<Food> menuItems = foodRepository.findByMenu_Id(menuId, pageable);
            return menuItems.map(foodMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Fetching menu items failed: " + e.getMessage());
        }
    }

    public FoodResponse createFood(Integer menuId, FoodRequest request) {
        String newImageUrl = null;
        try {
            Food food = foodMapper.toEntity(request);
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                newImageUrl = cloudinaryService.uploadFile(request.getImage());
            }
            food.setMenu(menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu not found for id: " + menuId)));
            food.setRemainingQuantity(request.getDefaultQuantity());
            food.setImageUrl(newImageUrl);
            return foodMapper.toDTO(foodRepository.saveAndFlush(food));
        } catch (Exception e) {
            if (newImageUrl != null) {
                try {
                    cloudinaryService.deleteFile(newImageUrl);
                } catch (Exception deleteException) {
                    throw new RuntimeException("Error when delete file");
                }
            }
            throw new RuntimeException("Creating menu item failed: " + e.getMessage());
        }
    }

    public FoodResponse updateFood(Integer menuId, Long foodId, FoodRequest request) {
        String newImageUrl = null;
        try {

            Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu not found for id: " + menuId));
            if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
                cloudinaryService.deleteFile(food.getImageUrl());
            }
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                newImageUrl = cloudinaryService.uploadFile(request.getImage());
            }
            food.setMenu(menu);
            food.setImageUrl(newImageUrl);
            foodMapper.updateEntityFromDTO(food, request);
            return foodMapper.toDTO(foodRepository.saveAndFlush(food));
        } catch (Exception e) {
            if (newImageUrl != null) {
                try {
                    cloudinaryService.deleteFile(newImageUrl);
                } catch (Exception deleteException) {
                    throw new RuntimeException("Error when delete file");
                }
            }
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }

    public void updateFoodActive(Long foodId, boolean active) {
        try {
            Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            food.setActive(active);
            foodRepository.saveAndFlush(food);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }

    public void toggleFoodLikeStatus(String customerId, Long foodId) {
        try {
            Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            Optional<FavoriteFood> favoriteFood = favoriteFoodRepository.findByCustomerIdAndFood_Id(customerId, foodId);
            if (favoriteFood.isPresent()) {
                food.setTotalLikes(food.getTotalLikes() - 1);
                favoriteFoodRepository.delete(favoriteFood.get());
            }else {
                FavoriteFood newFavoriteFood = FavoriteFood.builder().food(food).customerId(customerId).build();
                food.setTotalLikes(food.getTotalLikes() + 1);
                favoriteFoodRepository.saveAndFlush(newFavoriteFood);
            }
            foodRepository.saveAndFlush(food);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }
}
