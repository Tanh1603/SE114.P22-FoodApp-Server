package io.foodapp.server.services.Ai;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Ai.ChatMessageRequest;
import io.foodapp.server.dtos.Ai.ChatMessageResponse;
import io.foodapp.server.mappers.Ai.ChatMessageMapper;
import io.foodapp.server.models.AiModel.ChatMessage;
import io.foodapp.server.models.enums.Sender;
import io.foodapp.server.repositories.Ai.ChatMessageRepository;
import io.foodapp.server.utils.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    public Page<ChatMessageResponse> getMessagesOfCurrentUser(Pageable pageable) {

        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByCreatedBy(
            AuthUtils.getCurrentUserUid(),
            pageable
        );

        return chatMessages.map(chatMessageMapper::toDTO);
    }

    @Transactional
    public ChatMessageResponse createMessage(ChatMessageRequest request, Sender sender) {
        ChatMessage chatMessage = chatMessageMapper.toEntity(request);
        chatMessage.setSender(sender);
        return chatMessageMapper.toDTO(chatMessageRepository.save(chatMessage));
    }
}
