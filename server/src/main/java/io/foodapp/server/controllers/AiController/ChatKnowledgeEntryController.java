package io.foodapp.server.controllers.AiController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryRequest;
import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryResponse;
import io.foodapp.server.services.AiService.ChatKnowledEntryService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-knowledge-entrys")
public class ChatKnowledgeEntryController {
    private final ChatKnowledEntryService chatKnowledEntryService;

    @GetMapping
    public ResponseEntity<List<ChatKnowledgeEntryResponse>> getAll() {
        List<ChatKnowledgeEntryResponse> chatKnowledgeEntryResponses = chatKnowledEntryService.getAllChatKnowledgeEntrys();

        return ResponseEntity.ok(chatKnowledgeEntryResponses);
    }

    @PostMapping
    public ResponseEntity<ChatKnowledgeEntryResponse> create(@RequestBody ChatKnowledgeEntryRequest request) {
        ChatKnowledgeEntryResponse created = chatKnowledEntryService.createChatKnowledgeEntry(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatKnowledgeEntryResponse> update(@PathVariable Long id, @RequestBody ChatKnowledgeEntryRequest request) {
        ChatKnowledgeEntryResponse updated = chatKnowledEntryService.updateChatKnowledgeEntry(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        chatKnowledEntryService.deleteChatKnowledgeEntry(id);
        return ResponseEntity.noContent().build();
    }
}
