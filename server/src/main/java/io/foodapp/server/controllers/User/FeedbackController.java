package io.foodapp.server.controllers.User;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.foodapp.server.dtos.User.FeedbackRequest;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.services.User.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Validated
public class FeedbackController {
    private final FeedbackService feedbackService;

    // feedbacks
    @GetMapping("/order-items/{orderItemId}")
    public ResponseEntity<FeedbackResponse> getFeedbackByOrderItem(@PathVariable Long orderItemId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByOrderItem(orderItemId));
    }

    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FeedbackResponse> createFeedback(@Valid @ModelAttribute FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FeedbackResponse> updateFeedback(@PathVariable Long id,
            @Valid @ModelAttribute FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request));
    }

    @PatchMapping(value = "/{id}/images/add", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<FeedbackResponse> addFeedbackImages(@PathVariable Long id,
            @RequestParam List<MultipartFile> images) {
        return ResponseEntity.ok(feedbackService.addFeedbackImage(id, images));
    }

    @PatchMapping("/{id}/images/delete")
    public ResponseEntity<FeedbackResponse> deleteFeedbackImage(@PathVariable Long id,
            @RequestBody Map<String, List<String>> request) {
        List<String> publicId = request.get("publicIds");
        if (publicId == null || publicId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(feedbackService.deleteFeedbackImages(id, publicId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

}
