package io.foodapp.server.services.Ai;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryRequest;
import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryResponse;
import io.foodapp.server.mappers.Ai.ChatKnowledgeEntryMapper;
import io.foodapp.server.models.AiModel.ChatKnowledgeEntry;
import io.foodapp.server.models.AiModel.IntentType;
import io.foodapp.server.repositories.Ai.ChatKnowledgeEntryRepository;
import io.foodapp.server.repositories.Ai.IntentTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ChatKnowledEntryService {
    private final IntentTypeRepository intentTypeRepository;
    private final ChatKnowledgeEntryRepository chatKnowledgeEntryRepository;
    private final ChatKnowledgeEntryMapper chatKnowledgeEntryMapper;

    public List<ChatKnowledgeEntryResponse> getAllChatKnowledgeEntrys() {
        try {
            return chatKnowledgeEntryMapper.toDTOs(chatKnowledgeEntryRepository.findAll());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ChatKnowledgeEntry data: " + e.getMessage());
        }
    }

    public ChatKnowledgeEntryResponse getChatKnowledgeEntryById(Long id) {
        try {
            return chatKnowledgeEntryMapper.toDTO(chatKnowledgeEntryRepository.findById(id).orElseThrow());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ChatKnowledgeEntry data: " + e.getMessage());
        }
    }

    @Transactional
    public ChatKnowledgeEntryResponse createChatKnowledgeEntry(ChatKnowledgeEntryRequest request) {
        try {
            ChatKnowledgeEntry entity = chatKnowledgeEntryMapper.toEntity(request);

            IntentType intentType = intentTypeRepository.findById(request.getIntentTypeId())
                .orElseThrow(() -> new RuntimeException("Intent type not found with id: " + request.getIntentTypeId()));
            
            entity.setIntentType(intentType);

            return chatKnowledgeEntryMapper.toDTO(chatKnowledgeEntryRepository.save(entity));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ChatKnowledgeEntry data: " + e.getMessage());
        }
    }    

    @Transactional
    public ChatKnowledgeEntryResponse updateChatKnowledgeEntry(Long id, ChatKnowledgeEntryRequest request) {
        try {
            ChatKnowledgeEntry entity = chatKnowledgeEntryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ChatKnowledgeEntry not found"));
    
            // 1. Cập nhật các trường từ DTO (ngoại trừ unit)
            chatKnowledgeEntryMapper.updateEntityFromDto(request, entity);
    
            // 2. Gán lại Unit từ DB nếu có unitId
            if (request.getIntentTypeId() != null) {
                IntentType intentType = intentTypeRepository.findById(request.getIntentTypeId())
                .orElseThrow(() -> new RuntimeException("Intent type not found with id: " + request.getIntentTypeId()));
                entity.setIntentType(intentType);
            }
    
            // 3. Lưu và trả về DTO
            return chatKnowledgeEntryMapper.toDTO(chatKnowledgeEntryRepository.save(entity));
    
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ChatKnowledgeEntry data: " + e.getMessage());

        }
    }
    
    @Transactional
    public void deleteChatKnowledgeEntry(Long id) {
        try {
            ChatKnowledgeEntry entity = chatKnowledgeEntryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ChatKnowledgeEntry not found"));
                
            chatKnowledgeEntryRepository.delete(entity);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching ChatKnowledgeEntry data: " + e.getMessage());
        }
    }
}
