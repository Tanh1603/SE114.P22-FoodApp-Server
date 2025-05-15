package io.foodapp.server.llm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatCompletionRequest {
    private String model;
    private List<MistralChatMessage> messages;
    private Double temperature;
    private Integer max_tokens;

}

