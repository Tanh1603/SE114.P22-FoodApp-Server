package io.foodapp.server.services.ai;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.llm.prompts.MistralPromptBuilder;
import io.foodapp.server.mappers.Menu.FoodMapper;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import io.foodapp.server.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class MistralAiService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    private final MistralAiChatModel mistralAiChatModel;
    private final MistralPromptBuilder mistralPromptBuilder;

    public List<FoodResponse> suggestFoodsForCurrentUser() {
        String uid = AuthUtils.getCurrentUserUid();

        List<Order> orders = orderRepository.findAllByCreatedBy(uid);

        List<Food> allFoods = foodRepository.findAll().stream()
                .filter(Food::isActive)
                .collect(Collectors.toList());

        String prompt = mistralPromptBuilder.buildFoodSuggestionPrompt(orders, allFoods);

        // Gọi AI và nhận phản hồi JSON
        String aiResponse = mistralAiChatModel.call(prompt);

        // Parse JSON thành List<Long>
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> foodIds;
        try {
            foodIds = objectMapper.readValue(aiResponse, new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Không parse được danh sách món ăn từ AI: " + aiResponse, e);
        }

        return foodMapper.toDTOs(foodRepository.findAllById(foodIds));
    }

    //Dùng để test prompt
    public String getPrompt(String prompt) {
        String uid = AuthUtils.getCurrentUserUid();

        List<Order> orders = orderRepository.findAllByCreatedBy(uid);

        List<Food> allFoods = foodRepository.findAll().stream()
                .filter(Food::isActive)
                .collect(Collectors.toList());

        String prompt1 = mistralPromptBuilder.buildFoodSuggestionPrompt(orders, allFoods);

        // Gọi AI và nhận phản hồi JSON
        return prompt1;
    }

    public Flux<ChatResponse> generateStream(String message) {
        return null;
        //return mistralAiChatModel.stream(message);
    }
}