package io.foodapp.server.controllers.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.User.FeedbackRequest;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.services.User.FeedbackService;
import lombok.RequiredArgsConstructor;

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
