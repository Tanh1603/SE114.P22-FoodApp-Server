package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.FoodFilter;
import io.foodapp.server.dtos.Menu.FoodRequest;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.dtos.Menu.MenuRequest;
import io.foodapp.server.dtos.Menu.MenuResponse;
import io.foodapp.server.dtos.Specification.FoodSpecification;
import io.foodapp.server.mappers.Menu.FoodMapper;
import io.foodapp.server.models.MenuModel.FavoriteFood;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.repositories.Menu.FavoriteFoodRepository;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.services.CloudinaryService;
import io.foodapp.server.utils.ImageInfo;
import io.foodapp.server.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "MenuService")
public class MenuService {
    private final FoodRepository foodRepository;
    private final MenuRepository menuRepository;
    private final FoodMapper foodMapper;
    private final FavoriteFoodRepository favoriteFoodRepository;
    private final CloudinaryService cloudinaryService;
    private final String customerId = SecurityUtils.getCurrentCustomerId();

    public List<MenuResponse> getMenus(Boolean active, String name) {
        try {
            List<Menu> menus = menuRepository.findAll();

            if (active != null) {
                menus = menus.stream()
                        .filter(menu -> menu.isActive() == active)
                        .toList();
            }

            if (name != null && !name.isBlank()) {
                String lowerName = name.toLowerCase();
                menus = menus.stream()
                        .filter(menu -> menu.getName() != null && menu.getName().toLowerCase().contains(lowerName))
                        .toList();
            }

            return menus.stream().map(menu -> MenuResponse.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .active(menu.isActive())
                    .build()).toList();
        } catch (Exception e) {
            throw new RuntimeException("Fetching menus failed: " + e.getMessage());
        }
    }

    public MenuResponse createMenu(MenuRequest request) {
        try {
            Menu newMenu = menuRepository.saveAndFlush(Menu.builder().name(request.getName()).build());
            return MenuResponse.builder().name(newMenu.getName()).id(newMenu.getId()).active(newMenu.isActive())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Creating menu failed: " + e.getMessage());
        }
    }

    public MenuResponse updateMenu(Integer id, MenuRequest request) {
        try {
            Menu update = menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu not found for id: " + id));
            update.setName(request.getName());
            return MenuResponse.builder().name(update.getName()).id(update.getId()).active(update.isActive()).build();
        } catch (Exception e) {
            throw new RuntimeException("Updating menu failed: " + e.getMessage());
        }
    }

    public void updateMenuActive(Integer id) {
        try {
            Menu update = menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu not found for id: " + id));
            boolean active = !update.isActive();
            update.setActive(active);
            update.getFoods().forEach(food -> food.setActive(active));
            menuRepository.saveAndFlush(update);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu failed: " + e.getMessage());
        }
    }

    // Menu item
    public Page<FoodResponse> getFood(FoodFilter filter, Pageable pageable) {
        try {
            Specification<Food> specification = FoodSpecification.withFilter(filter);
            Page<Food> menuItems = foodRepository.findAll(specification, pageable);
            return menuItems.map(food -> {
                FoodResponse dto = foodMapper.toDTO(food);
                if (customerId != null) {
                    boolean liked = favoriteFoodRepository.findByCustomerIdAndFood_Id(customerId, food.getId())
                            .isPresent();
                    dto.setLiked(liked);
                }
                return dto;
            });
        } catch (Exception e) {
            throw new RuntimeException("Fetching menu items failed: " + e.getMessage());
        }
    }

    public Page<FoodResponse> getFavoriteFood(Pageable pageable) {
        try {
            Page<FavoriteFood> favorites = favoriteFoodRepository.findByCustomerId(customerId, pageable);

            return favorites.map(favorite -> {
                Food food = favorite.getFood();
                FoodResponse dto = foodMapper.toDTO(food);
                dto.setLiked(true);
                return dto;
            });
        } catch (Exception e) {
            throw new RuntimeException("Fetching favorite foods failed: " + e.getMessage());
        }
    }

    public FoodResponse createFood(FoodRequest request) {
        List<ImageInfo> newImages = null;
        try {
            Integer menuId = request.getMenuId();
            Food food = foodMapper.toEntity(request);
            food.setMenu(menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("Menu not found for id: " + menuId)));

            newImages = cloudinaryService.uploadMultipleImage(request.getImages());

            food.setImages(newImages);
            food.setRemainingQuantity(request.getDefaultQuantity());
            return foodMapper.toDTO(foodRepository.saveAndFlush(food));
        } catch (Exception e) {
            if (newImages != null && !newImages.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleImage(newImages.stream().map(ImageInfo::getUrl).toList());
                } catch (Exception ex) {
                    throw new RuntimeException("Creating menu item failed: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Creating menu item failed: " + e.getMessage());
        }
    }

    public FoodResponse updateFood(Long foodId, FoodRequest request) {
        List<ImageInfo> updatedImages = null;
        try {
            Integer menuId = request.getMenuId();
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("Menu not found for id: " + menuId));

            if (request.getImages() != null && !request.getImages().isEmpty() && request.getImages().stream()
                    .anyMatch(file -> !file.isEmpty())) {
                updatedImages = cloudinaryService.uploadMultipleImage(request.getImages());
                if (food.getImages() != null) {
                    log.info("Deleting old images: {}", food.getImages().stream().map(ImageInfo::getPublicId).toList());
                    cloudinaryService
                            .deleteMultipleImage(food.getImages().stream().map(ImageInfo::getPublicId).toList());
                }
                food.setImages(updatedImages);
            }

            food.setMenu(menu);
            foodMapper.updateEntityFromDTO(food, request);
            return foodMapper.toDTO(foodRepository.saveAndFlush(food));
        } catch (Exception e) {
            log.error("Update food failed: {}", e.getMessage());
            if (updatedImages != null && !updatedImages.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleImage(updatedImages.stream().map(ImageInfo::getUrl).toList());
                } catch (Exception ex) {
                    throw new RuntimeException("Updating menu item failed: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }


    public void toggleFoodActive(Long foodId) {
        try {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            food.setActive(!food.isActive());
            foodRepository.saveAndFlush(food);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }

    public void toggleFoodLikeStatus(Long foodId) {
        try {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found for id " + foodId));
            Optional<FavoriteFood> favoriteFood = favoriteFoodRepository.findByCustomerIdAndFood_Id(customerId, foodId);
            if (favoriteFood.isPresent()) {
                food.setTotalLikes(food.getTotalLikes() - 1);
                favoriteFoodRepository.delete(favoriteFood.get());
            } else {
                FavoriteFood newFavoriteFood = FavoriteFood.builder().food(food).customerId(customerId).build();
                food.setTotalLikes(food.getTotalLikes() + 1);
                favoriteFoodRepository.saveAndFlush(newFavoriteFood);
            }
            foodRepository.saveAndFlush(food);
        } catch (Exception e) {
            throw new RuntimeException("Updating menu item failed: " + e.getMessage());
        }
    }

    public List<FoodResponse> getPopularFoods() {
        try {
            List<Food> popularFoods = foodRepository.findTop10ByOrderByTotalLikesDesc();
            return popularFoods.stream().map(foodMapper::toDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException("Fetching popular foods failed: " + e.getMessage());
        }
    }
}
