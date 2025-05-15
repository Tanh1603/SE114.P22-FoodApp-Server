package io.foodapp.server.llm.client;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.util.concurrent.RateLimiter;

import io.foodapp.server.llm.dto.ChatCompletionRequest;
import io.foodapp.server.llm.dto.ChatCompletionResponse;
import io.foodapp.server.llm.dto.MistralChatMessage;
import io.foodapp.server.llm.dto.ChatMessageBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MistralClient {
    private final Environment environment;

    private String apiKey;

    private String model;

    private final String url = "https://api.mistral.ai/v1/chat/completions";

    private final RateLimiter rateLimiter = RateLimiter.create(1.0);
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        this.apiKey = environment.getProperty("SPRING_AI_MISTRALAI_API_KEY");
        this.model = environment.getProperty("SPRING_AI_OPENAI_CHAT_OPTIONS_MODEL");
    }

    public String chatWithMistral(String data, String question) {
        rateLimiter.acquire();

        List<MistralChatMessage> messages = List.of(
                ChatMessageBuilder.systemPrompt(data),
                ChatMessageBuilder.userPrompt(question));

        ChatCompletionRequest request = new ChatCompletionRequest(model, messages, 0.5, 4000);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatCompletionResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, ChatCompletionResponse.class);

        ChatCompletionResponse body = response.getBody();
        return body != null ? body.getFirstMessageContent() : "No content";

    }

}
