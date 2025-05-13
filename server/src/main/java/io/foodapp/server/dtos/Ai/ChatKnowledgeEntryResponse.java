package io.foodapp.server.dtos.Ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatKnowledgeEntryResponse {
    private Long id;
    private String title;
    private String content;
    private IntentTypeResponse intentType; // Thông tin IntentType
}
