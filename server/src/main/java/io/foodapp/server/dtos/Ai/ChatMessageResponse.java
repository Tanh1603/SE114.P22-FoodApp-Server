package io.foodapp.server.dtos.Ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long id;
    private String message;
    private String sender;
    private String createdBy;
    private String createdAt;
}
