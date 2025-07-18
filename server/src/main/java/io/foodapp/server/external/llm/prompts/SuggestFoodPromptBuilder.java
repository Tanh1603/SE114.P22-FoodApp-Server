package io.foodapp.server.external.llm.prompts;

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

    public static final String SUGGEST_CONTEXT_PROMPT= """
                Từ dữ liệu cung cấp từ user
                Yêu cầu:
                    - Trả về đúng một mảng JSON gồm **10 id món ăn được gợi ý**, định dạng như sau (ví dụ):
                      `[12, 5, 8, 3, 19, 7, 2, 1, 4, 6]`
                    - Gợi ý dựa trên các món ăn khách đã từng đặt:
                      + Ưu tiên các món đã nhận được phản hồi tích cực (ví dụ: "Great taste!", "Perfect").
                      + Tránh gợi ý các món bị phản hồi tiêu cực (ví dụ: "Too salty").
                      + Có thể chọn lại các món chưa phản hồi nếu chúng có rating cao.
                    - Nếu khách chưa từng đặt món nào, hãy gợi ý dựa theo rating từ cao đến thấp.
                    - Nếu nhiều món có cùng rating, chọn ngẫu nhiên.
                    - Kết quả phải được sắp xếp theo **rating giảm dần**.
                    - Nếu không có đủ món ăn thì có thể trả về ít hơn 10 món.
                    - **Chỉ trả về mảng số duy nhất**, không thêm bất kỳ giải thích nào.
                """;

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

    public String buildFoodOrderPrompt(List<Order> orders) {
        StringBuilder prompt = new StringBuilder();

        Map<Long, FoodOrderInfo> foodOrderInfoMap = new HashMap<>();

        if(orders == null || orders.isEmpty()) {
            return "Danh sách món ăn khách đã từng đặt: Hiện chưa có dữ liệu.";
        }

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                Food food = item.getFood();
                long foodId = food.getId();

                Feedback feedback = feedbackRepository.findByOrderItemId(item.getId()).orElseThrow(
                        () -> new RuntimeException("Feedback not found for order item ID: " + item.getId()));

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

        return prompt.toString();
    }

}
