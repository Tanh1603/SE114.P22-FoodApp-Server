package io.foodapp.server.services.Ai;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Ai.IntentTypeRequest;
import io.foodapp.server.dtos.Ai.IntentTypeResponse;
import io.foodapp.server.mappers.Ai.IntentTypeMapper;
import io.foodapp.server.models.AiModel.IntentType;
import io.foodapp.server.repositories.Ai.IntentTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntentTypeService {
    private final IntentTypeRepository intentTypeRepository;
    private final IntentTypeMapper intentTypeMapper;
    
    public List<IntentTypeResponse> getAllIntentTypes() {
        try {
            return intentTypeMapper.toDTOs(intentTypeRepository.findAll());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching intent type data: " + e.getMessage());
        }
    }

    @Transactional
    public IntentTypeResponse createIntentType(IntentTypeRequest request) {
        try {
            IntentType saved = intentTypeRepository.save(intentTypeMapper.toEntity(request));
            return intentTypeMapper.toDTO(saved);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching intent type data: " + e.getMessage());
        }
    }

    @Transactional
    public IntentTypeResponse updateIntentType(Integer id, IntentTypeRequest request) {
        try {
            IntentType entity = intentTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Intent type not found"));
            intentTypeMapper.updateEntityFromDto(request, entity);
            return intentTypeMapper.toDTO(intentTypeRepository.save(entity));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching intent type data: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteIntentType(Integer id) {
        try {
            IntentType intentType = intentTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Intent type not found with id: " + id));

            if (intentType.getChatKnowledgeEntrys() != null && !intentType.getChatKnowledgeEntrys().isEmpty()) {
                throw new RuntimeException("Cannot delete intent type because it's having ChatKnowledEntrys.");
            }

            intentTypeRepository.delete(intentType);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }
}
