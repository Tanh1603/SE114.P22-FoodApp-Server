package io.foodapp.server.controllers.User;

import io.foodapp.server.dtos.Filter.PageFilter;
import io.foodapp.server.dtos.User.FeedbackRequest;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.User.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    // feedbacks
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FeedbackResponse> createFeedback(@ModelAttribute FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FeedbackResponse> updateFeedback(@PathVariable Long id, @ModelAttribute FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

}
