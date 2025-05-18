package io.foodapp.server.external.llm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MistralChatMessage {
    private String role;
    private String content;
}
