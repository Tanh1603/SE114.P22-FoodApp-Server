package io.foodapp.server.controllers.menu;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.foodapp.server.dtos.Filter.FoodFilter;
import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.Menu.FoodRequest;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Menu.MenuService;
import io.foodapp.server.services.User.FeedbackService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FeedbackService feedbackService;
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<PageResponse<FoodResponse>> getFood(
            @ModelAttribute PageFilter pageFilter,
            @ModelAttribute FoodFilter foodFilter) {
        Page<FoodResponse> response = menuService.getFood(foodFilter, PageFilter.toPageAble(pageFilter));

        return ResponseEntity.ok(PageResponse.fromPage(response));
    }

    @GetMapping("/populars")
    public ResponseEntity<List<FoodResponse>> getPopularFoods() {
        return ResponseEntity.ok(menuService.getPopularFoods());
    }

    @GetMapping("/{foodId}/feedbacks")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByFoodId(
            @PathVariable Long foodId, @ModelAttribute PageFilter filter) {
        Pageable pageable = PageFilter.toPageAble(filter);
        Page<FeedbackResponse> responses = feedbackService.getFeedbacksByFoodId(foodId, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @GetMapping("/favorite")
    public PageResponse<FoodResponse> getFavoriteFoodsByCustomerId(@ModelAttribute PageFilter filter) {
        Pageable pageable = PageFilter.toPageAble(filter);
        return PageResponse.fromPage(menuService.getFavoriteFood(pageable));
    }

    @PostMapping("/{foodId}/like-toggle")
    public ResponseEntity<Boolean> toggleFoodLikeStatus(@PathVariable Long foodId) {
        return ResponseEntity.ok(menuService.toggleFoodLikeStatus(foodId));
    }

    @PatchMapping("/{foodId}/status-toggle")
    public ResponseEntity<Void> updateFoodStatus(@PathVariable Long foodId) {
        menuService.toggleFoodActive(foodId);
        return ResponseEntity.noContent().build();
    }

    // admin
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FoodResponse> createFood(@ModelAttribute FoodRequest request) {
        return ResponseEntity.ok(menuService.createFood(request));
    }

    @PutMapping(value = "/{foodId}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FoodResponse> updateFood(@ModelAttribute FoodRequest request,
            @PathVariable Long foodId) {
        return ResponseEntity.ok(menuService.updateFood(foodId, request));
    }
}
