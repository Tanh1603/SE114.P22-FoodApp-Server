package io.foodapp.server.controllers.menu;

import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.User.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FeedbackService feedbackService;

    @GetMapping("/{foodId}/feedbacks")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByFoodId(
            @PathVariable Long foodId, @ModelAttribute PageFilter filter) {
        Pageable pageable = PageFilter.toPageAble(filter);
        Page<FeedbackResponse> responses = feedbackService.getFeedbacksByFoodId(foodId, pageable);
        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }
}
