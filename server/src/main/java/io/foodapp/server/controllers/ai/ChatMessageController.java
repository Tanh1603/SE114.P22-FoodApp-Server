package io.foodapp.server.controllers.Ai;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Ai.ChatMessageResponse;
import io.foodapp.server.dtos.responses.PageResponse;
import io.foodapp.server.services.Ai.ChatMessageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-messages")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<PageResponse<ChatMessageResponse>> getMessages(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ChatMessageResponse> pageResult = chatMessageService.getMessagesOfCurrentUser(pageable);

        PageResponse<ChatMessageResponse> response = PageResponse.<ChatMessageResponse>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .first(pageResult.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }


}
