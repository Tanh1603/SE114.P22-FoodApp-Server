package io.foodapp.server.llm.prompts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.models.User.Feedback;
import io.foodapp.server.repositories.User.FeedbackRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SuggestFoodPromptBuilder {
    private final FeedbackRepository feedbackRepository;

    private static class FoodOrderInfo {
        Food food;
        int totalQuantity;
        List<String> feedbacks = new ArrayList<>();

        public FoodOrderInfo(Food food, int quantity, String feedback) {
            this.food = food;
            this.totalQuantity = quantity;
            if (feedback != null) {
                this.feedbacks.add(feedback);
            }
        }
    }

    public String buildFoodSuggestionPrompt(List<Order> orders, List<Food> allFoods) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Bạn là trợ lý AI cho app quản lý đặt món ăn.\n");

        Map<Long, FoodOrderInfo> foodOrderInfoMap = new HashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                Food food = item.getFood();
                long foodId = food.getId();

                Feedback feedback = feedbackRepository.findByOrderItemId(item.getId());

                foodOrderInfoMap.compute(foodId, (id, existing) -> {
                    if (existing == null) {
                        return new FoodOrderInfo(food, item.getQuantity(),
                                feedback != null ? feedback.getContent() : null);
                    } else {
                        existing.totalQuantity += item.getQuantity();
                        if (feedback != null) {
                            existing.feedbacks.add(feedback.getContent());
                        }
                        return existing;
                    }
                });
            }
        }

        // Viết vào prompt
        prompt.append("Danh sách món ăn khách đã từng đặt:\n");
        for (FoodOrderInfo info : foodOrderInfoMap.values()) {
            prompt.append("- ").append(info.food.getName()).append(" (")
                    .append(info.totalQuantity).append("): ");

            if (!info.feedbacks.isEmpty()) {
                prompt.append(String.join("; ", info.feedbacks));
            } else {
                prompt.append("Chưa có phản hồi");
            }

            prompt.append("\n");
        }

        prompt.append("Danh sách các món ăn có thể đặt của quán ăn:\n");
        for (Food food : allFoods) {
            prompt.append("- ").append("Id: ").append(food.getId())
                    .append(", Tên: ").append(food.getName())
                    .append(", Giá: ").append(food.getPrice())
                    .append(", Mô tả: ").append(food.getDescription())
                    .append(", Rating: ").append(food.getTotalRating())
                    .append("\n");
        }

        prompt.append("""
            Yêu cầu:
                - Trả về đúng một mảng JSON gồm **10 id món ăn được gợi ý**, định dạng như sau (ví dụ):
                  `[12, 5, 8, 3, 19, 7, 2, 1, 4, 6]`
                - Gợi ý dựa trên các món ăn khách đã từng đặt:
                  + Ưu tiên các món đã nhận được phản hồi tích cực (ví dụ: "Great taste!", "Perfect").
                  + Loại trừ hoặc giảm ưu tiên các món bị phản hồi tiêu cực (ví dụ: "Too salty").
                  + Có thể chọn lại các món chưa phản hồi nếu chúng có rating cao.
                - Nếu khách chưa từng đặt món nào, hãy gợi ý dựa theo rating từ cao nhất.
                - Nếu nhiều món có cùng rating, chọn ngẫu nhiên.
                - Kết quả phải được sắp xếp theo **rating giảm dần**.
                - Nếu không có đủ món ăn thì có thể trả về ít hơn 10 món.
                - **Chỉ trả về mảng số duy nhất**, không thêm bất kỳ giải thích nào.
            """);

        return prompt.toString();
    }

    
}
