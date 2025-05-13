package io.foodapp.server.mappers.Ai;

import java.util.List;

import org.mapstruct.Mapper;

import io.foodapp.server.dtos.Ai.ChatMessageRequest;
import io.foodapp.server.dtos.Ai.ChatMessageResponse;
import io.foodapp.server.models.AiModel.ChatMessage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChatMessageMapper {
    ChatMessage toEntity(ChatMessageRequest dto);
    ChatMessageResponse toDTO(ChatMessage entity);

    List<ChatMessage> toEntities(List<ChatMessageRequest> dtos);
    List<ChatMessageResponse> toDTOs(List<ChatMessage> entities);
}
