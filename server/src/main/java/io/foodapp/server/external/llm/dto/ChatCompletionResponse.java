package io.foodapp.server.external.llm.dto;

import java.util.List;

import lombok.Data;

@Data
public class ChatCompletionResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    public String getFirstMessageContent() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getMessage().getContent();
        }
        return "No content";
    }
}

