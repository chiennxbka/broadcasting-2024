package com.mintpot.broadcasting.rest;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.request.ChatMessageRequest;
import com.mintpot.broadcasting.common.response.ChatNotification;
import com.mintpot.broadcasting.service.chat.ChatMessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"[Admin APIs] Chat management"})
public class ChatController extends AbstractController {
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageRequest chatMessage) {
        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
            ChatNotification.builder().id(saved.getId()).senderId(chatMessage.getSenderId()).build());
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
        @PathVariable Long senderId,
        @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long senderId,
                                                              @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<ChatMessage> findMessage(@PathVariable int id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }
}
