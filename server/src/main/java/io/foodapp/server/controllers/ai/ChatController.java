package io.foodapp.server.controllers.Ai;

import java.util.List;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.services.Ai.MistralAiService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mistral-ai")
public class ChatController {
    private final MistralAiService mistralAiService;

    @GetMapping("/suggest-foods")
    public List<FoodResponse> suggestFoodsForCurrentUser() {
        return this.mistralAiService.suggestFoodsForCurrentUser();
    }

    @GetMapping("/chat")
	public Flux<ChatResponse> chatFlux(@RequestParam(value = "message") String message) {
        return mistralAiService.chatFlux(message);
    }

    // @GetMapping("/prompt")
    // public String getPrompt(@RequestParam(value = "message") String message) {
    //     return mistralAiService.getPrompt(message);
    // }
}
