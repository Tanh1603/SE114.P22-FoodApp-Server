package io.foodapp.server.mappers.Ai;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryRequest;
import io.foodapp.server.dtos.Ai.ChatKnowledgeEntryResponse;
import io.foodapp.server.models.AiModel.ChatKnowledgeEntry;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChatKnowledgeEntryMapper {
    ChatKnowledgeEntry toEntity(ChatKnowledgeEntryRequest dto);
    ChatKnowledgeEntryResponse toDTO(ChatKnowledgeEntry entity);

    List<ChatKnowledgeEntry> toEntities(List<ChatKnowledgeEntryRequest> dtos);
    List<ChatKnowledgeEntryResponse> toDTOs(List<ChatKnowledgeEntry> entities);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ChatKnowledgeEntryRequest chatKnowledgeEntryRequest, @MappingTarget ChatKnowledgeEntry chatKnowledgeEntry);
}
