package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Menu.MenuService;
import io.foodapp.server.services.User.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FeedbackService feedbackService;
    private final MenuService menuService;

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
    public ResponseEntity<Void> toggleFoodLikeStatus(@PathVariable Long foodId) {
        menuService.toggleFoodLikeStatus(foodId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{foodId}/status-toggle")
    public ResponseEntity<Void> updateFoodStatus(@PathVariable Long foodId) {
        menuService.toggleFoodActive(foodId);
        return ResponseEntity.noContent().build();
    }
}
