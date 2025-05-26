package io.foodapp.server.controllers.AiController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Ai.ChatMessageRequest;
import io.foodapp.server.dtos.Ai.ChatMessageResponse;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.services.AiService.MistralAiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mistral-ai")
public class MistralAiController {
    private final MistralAiService mistralAiService;

    @GetMapping("/suggest-foods")
    public List<FoodResponse> suggestFoodsForCurrentUser() {
        return this.mistralAiService.suggestFoodsForCurrentUser();
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatMessageResponse> chat(@RequestBody ChatMessageRequest message) {
        ChatMessageResponse response = mistralAiService.chat(message);
        return ResponseEntity.ok(response);
    }

        @PostMapping("/prompt")
    public ResponseEntity<String> prompt(@RequestBody String userMessage) {
        String response = mistralAiService.getPrompt(userMessage);
        return ResponseEntity.ok(response);
    }
}
