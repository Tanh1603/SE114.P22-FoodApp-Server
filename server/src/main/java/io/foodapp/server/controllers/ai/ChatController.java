package io.foodapp.server.controllers.ai;

import java.util.List;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.services.ai.MistralAiService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mistral-ai")
public class ChatController {

    private final MistralAiService suggestedFoodService;

    @GetMapping("/suggest-foods")
    public List<FoodResponse> suggestFoodsForCurrentUser() {
        return this.suggestedFoodService.suggestFoodsForCurrentUser();
    }

    @GetMapping("/ai/generateStream")
	public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return null;
    }

    @GetMapping("/prompt")
    public String getPrompt(String prompt) {
        return suggestedFoodService.getPrompt(prompt);
    }
}
