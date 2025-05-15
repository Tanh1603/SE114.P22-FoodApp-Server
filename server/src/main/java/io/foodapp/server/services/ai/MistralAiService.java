package io.foodapp.server.services.Ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.foodapp.server.dtos.Ai.ChatMessageRequest;
import io.foodapp.server.dtos.Ai.ChatMessageResponse;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.llm.client.MistralClient;
import io.foodapp.server.llm.prompts.ChatPromptBuilder;
import io.foodapp.server.llm.prompts.SuggestFoodPromptBuilder;
import io.foodapp.server.mappers.Menu.FoodMapper;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.Sender;
import io.foodapp.server.repositories.Menu.FoodRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import io.foodapp.server.utils.AuthUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MistralAiService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    private final MistralAiChatModel mistralAiChatModel;
    private final SuggestFoodPromptBuilder suggestFoodPromptBuilder;
    private final ChatPromptBuilder chatPromptBuilder;

    private final MistralClient mistralClient;
    private final ChatMessageService chatMessageService;

    public List<FoodResponse> suggestFoodsForCurrentUser() {
        String uid = AuthUtils.getCurrentUserUid();

        List<Order> orders = orderRepository.findAllByCreatedBy(uid);

        List<Food> allFoods = foodRepository.findAll().stream()
                .filter(Food::isActive)
                .collect(Collectors.toList());

        String prompt = suggestFoodPromptBuilder.buildFoodSuggestionPrompt(orders, allFoods);

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

    // Dùng để test prompt
    public String getPrompt(String message) {
        // List<Long> chatKnowledgeIds = new ArrayList<>();
        // chatKnowledgeIds.add(2L);
        // String intentPrompt = chatPromptBuilder.buildIntentAndKnowledgePrompt(message);
        // String aiResponse1 = mistralAiChatModel.call(intentPrompt);
        String aiResponse1 = chatPromptBuilder.buildVoucherPrompt();
        return aiResponse1;
        // return intentPrompt;
    }

    
    public ChatMessageResponse chat(ChatMessageRequest message) {
        chatMessageService.createMessage(message, Sender.USER);
        String intentPrompt = chatPromptBuilder.buildIntentAndKnowledgePrompt(message.getContent());
        String aiResponse1 = mistralAiChatModel.call(intentPrompt);
        String aiResponse2, contentPrompt;

        if ("FOOD".equals(aiResponse1.trim())) {
            contentPrompt = chatPromptBuilder.buildFoodPrompt();
            aiResponse2 = mistralClient.chatWithMistral(contentPrompt, message.getContent());

        } else if ("VOUCHER".equals(aiResponse1.trim())){
            contentPrompt = chatPromptBuilder.buildVoucherPrompt();
            aiResponse2 = mistralClient.chatWithMistral(contentPrompt, message.getContent());

        } else if (aiResponse1.matches("^\\[\\s*\\d+(\\s*,\\s*\\d+)*\\s*\\]$")) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> chatKnowledgeIds = new ArrayList<>();
            try {
                chatKnowledgeIds = objectMapper.readValue(aiResponse1, new TypeReference<List<Long>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error: " + aiResponse1, e);
            }

            contentPrompt = chatPromptBuilder.buildContentPrompt(chatKnowledgeIds);

            // Gọi Mistral AI qua client
            aiResponse2 = mistralClient.chatWithMistral(contentPrompt, message.getContent());

        } else {
            aiResponse2 = aiResponse1;
        }

        ChatMessageResponse response = chatMessageService
            .createMessage(ChatMessageRequest.builder().content(aiResponse2).build(), Sender.BOT);
        return response;
    }
}