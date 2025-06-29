package io.foodapp.server.services.User;

import io.foodapp.server.dtos.User.FeedbackRequest;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.mappers.User.FeedbackMapper;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.User.Feedback;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Order.OrderItemRepository;
import io.foodapp.server.repositories.User.FeedbackRepository;
import io.foodapp.server.services.CloudinaryService;
import io.foodapp.server.utils.ImageInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final OrderItemRepository orderItemRepository;
    private final FeedbackMapper feedbackMapper;
    private final FoodRepository foodRepository;
    private final CloudinaryService cloudinaryService;
    private final CustomerService customerService;

    public FeedbackResponse mapFeedbackWithUser(Feedback feedback) {
        FeedbackResponse response = feedbackMapper.toDTO(feedback);
        UserRecord userRecord = customerService.getCustomerDetails(feedback.getCustomerId());
        response.setDisplayName(
                userRecord.getDisplayName() != null ? userRecord.getDisplayName() : "Anonymous");
        response.setAvatar(userRecord.getPhotoUrl());
        return response;
    }

    public Page<FeedbackResponse> getFeedbacksByFoodId(Long id, Pageable pageable) {
        try {
            Page<Feedback> feedbacks = feedbackRepository.findByOrderItem_Food_Id(id, pageable);
            return feedbacks.map(feedback -> mapFeedbackWithUser(feedback));
        } catch (Exception e) {
            throw new RuntimeException("Error getting feedbacks: " + e.getMessage());
        }
    }

    public FeedbackResponse getFeedbackByOrderItem(Long orderItemId) {
        try {
            Feedback feedback = feedbackRepository.findByOrderItemId(orderItemId)
                    .orElseThrow(() -> new RuntimeException("Feedback not found for order item ID: " + orderItemId));
            return mapFeedbackWithUser(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Error getting feedback: " + e.getMessage());
        }
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        List<ImageInfo> images = null;
        try {
            var orderItem = orderItemRepository.findById(request.getOrderItemId())
                    .orElseThrow(() -> new RuntimeException("Order item not found"));
            if (feedbackRepository.existsByOrderItem(orderItem)) {
                throw new RuntimeException("Feedback for this order item already exists");
            }
            Feedback feedback = feedbackMapper.toEntity(request);
            Food food = orderItem.getFood();

            images = cloudinaryService.uploadMultipleImage(request.getImages());

            feedback.setImages(images);
            feedback.setOrderItem(orderItem);
            feedback.setCreatedAt(LocalDateTime.now());

            food.setTotalFeedbacks(food.getTotalFeedbacks() + 1);
            food.setTotalRating(food.getTotalRating() + request.getRating());
            foodRepository.save(food);

            return mapFeedbackWithUser(feedbackRepository.save(feedback));
        } catch (Exception e) {
            if (images != null && !images.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleImage(images.stream().map(ImageInfo::getPublicId).toList());
                } catch (Exception ex) {
                    throw new RuntimeException("Error when deleting files: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Error when create feedback: " + e.getMessage());
        }
    }

    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        List<ImageInfo> images = null;
        try {
            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            images = cloudinaryService.uploadMultipleImage(request.getImages());
            if (feedback.getImages() != null) {
                cloudinaryService
                        .deleteMultipleImage(feedback.getImages().stream().map(ImageInfo::getPublicId).toList());
            }

            feedback.setContent(request.getContent());
            feedback.setRating(request.getRating());
            feedback.setImages(images);

            Food food = feedback.getOrderItem().getFood();
            food.setTotalFeedbacks(food.getTotalFeedbacks() + 1);
            food.setTotalRating(food.getTotalRating() + request.getRating());
            foodRepository.save(food);

            return feedbackMapper.toDTO(feedbackRepository.save(feedback));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            if (images != null && !images.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleImage(images.stream().map(ImageInfo::getPublicId).toList());
                } catch (Exception io) {
                    throw new RuntimeException("Error when deleting files: " + e.getMessage());
                }
            }
            throw new RuntimeException("Error when update feedback: " + e.getMessage());
        }
    }

    public void deleteFeedback(Long id) {
        try {
            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            feedbackRepository.deleteById(id);

            Food food = feedback.getOrderItem().getFood();
            food.setTotalFeedbacks(food.getTotalFeedbacks() - 1);
            food.setTotalRating(food.getTotalRating() - feedback.getRating());
            foodRepository.save(food);

            if (feedback.getImages() != null && !feedback.getImages().isEmpty()) {
                cloudinaryService
                        .deleteMultipleImage(feedback.getImages().stream().map(ImageInfo::getPublicId).toList());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error when delete feedback: " + e.getMessage());
        }
    }
}
