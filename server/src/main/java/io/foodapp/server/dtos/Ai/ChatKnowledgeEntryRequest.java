package io.foodapp.server.dtos.Ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatKnowledgeEntryRequest {
    @NotBlank(message = "Title is required")
    private String title;    // Câu hỏi
    @NotBlank(message = "Content is required")
    private String content;      // Trả lời
    
    @NotNull(message = "IntentType ID is required")
    private Integer intentTypeId; // ID của IntentType
}
