package com.mintpot.broadcasting.service.chat;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.enums.MessageStatus;
import com.mintpot.broadcasting.common.request.ChatMessageRequest;

import java.util.List;

public interface ChatMessageService {

  ChatMessage save(ChatMessageRequest chatMessage);

  long countNewMessages(Long senderId, Long recipientId);

  List<ChatMessage> findChatMessages(Long senderId, Long recipientId);

  ChatMessage findById(Integer id);

  void updateStatuses(Long senderId, Long recipientId, MessageStatus status);
}
