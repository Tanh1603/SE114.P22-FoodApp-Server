package io.foodapp.server.controllers.Ai;

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
import io.foodapp.server.services.Ai.MistralAiService;
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

    @GetMapping("/prompt")
    public ResponseEntity<String> prompt() {
        String response = mistralAiService.getPrompt();
        return ResponseEntity.ok(response);
    }
}
