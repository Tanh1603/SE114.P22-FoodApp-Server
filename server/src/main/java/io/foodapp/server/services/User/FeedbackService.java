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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final OrderItemRepository orderItemRepository;
    private final FeedbackMapper feedbackMapper;
    private final FoodRepository foodRepository;
    private final CloudinaryService cloudinaryService;

    public Page<FeedbackResponse> getFeedbacksByFoodId(Long id, Pageable pageable) {
        try {
            Page<Feedback> feedbacks = feedbackRepository.findByOrderItem_Food_Id(id, pageable);
            return feedbacks.map(feedbackMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error getting feedbacks: " + e.getMessage());
        }
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        List<String> listImage = new ArrayList<>();
        try {
            var orderItem = orderItemRepository.findById(request.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Order item not found"));

            if (!request.getImage().isEmpty()) {
                listImage = cloudinaryService.uploadMultipleFile(request.getImage());
            }
            Feedback feedback = feedbackMapper.toEntity(request);
            feedback.setOrderItem(orderItem);
            feedback.setImageUrls(listImage);

            Food food = orderItem.getFood();
            food.setTotalFeedbacks(food.getTotalFeedbacks() + 1);
            food.setTotalRating(food.getTotalRating() + request.getRating());
            foodRepository.save(food);

            return feedbackMapper.toDTO(feedbackRepository.save(feedback));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            if (!listImage.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleFile(listImage);
                } catch (IOException io) {
                    throw new RuntimeException("Error when deleting files: " + e.getMessage());
                }
            }
            throw new RuntimeException("Error when create feedback: " + e.getMessage());
        }
    }

    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        List<String> listImage = new ArrayList<>();
        try {
            Feedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feedback not found"));

            if (feedback == null) {
                throw new RuntimeException("Feedback not found for this OrderItem");
            }
            if (!request.getImage().isEmpty()) {
                cloudinaryService.deleteMultipleFile(feedback.getImageUrls());
                listImage = cloudinaryService.uploadMultipleFile(request.getImage());
            }
            feedback.setContent(request.getContent());
            feedback.setRating(request.getRating());
            feedback.setImageUrls(listImage);

            Food food = feedback.getOrderItem().getFood();
            food.setTotalFeedbacks(food.getTotalFeedbacks() + 1);
            food.setTotalRating(food.getTotalRating() + request.getRating());
            foodRepository.save(food);

            return feedbackMapper.toDTO(feedbackRepository.save(feedback));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            if (!listImage.isEmpty()) {
                try {
                    cloudinaryService.deleteMultipleFile(listImage);
                } catch (IOException io) {
                    throw new RuntimeException("Error when deleting files: " + e.getMessage());
                }
            }
            throw new RuntimeException("Error when update feedback: " + e.getMessage());
        }
    }

    public void deleteFeedback(Long id) {
        try {
            feedbackRepository.deleteById(id);
            Optional<Feedback> feedback = feedbackRepository.findById(id);
            if (feedback.isPresent()) {
                Food food = feedback.get().getOrderItem().getFood();
                food.setTotalFeedbacks(food.getTotalFeedbacks() - 1);
                food.setTotalRating(food.getTotalRating() - feedback.get().getRating());
                foodRepository.save(food);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when delete feedback: " + e.getMessage());
        }
    }
}
