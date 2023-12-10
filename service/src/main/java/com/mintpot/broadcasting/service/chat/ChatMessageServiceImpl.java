package com.mintpot.broadcasting.service.chat;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.enums.MessageStatus;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.common.mapper.ChatMapper;
import com.mintpot.broadcasting.common.request.ChatMessageRequest;
import com.mintpot.broadcasting.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

  private final ChatMessageRepository repository;

  private final ChatRoomService chatRoomService;

  private final ChatMapper chatMapper;

  @Override
  public ChatMessage save(ChatMessageRequest chatMessage) {
    var chatId = UUID.randomUUID().toString();
    ChatMessage message = chatMapper.toEntity(chatMessage);
    message.setChatId(chatId);
    message.setStatus(MessageStatus.RECEIVED);
    repository.save(message);
    return message;
  }

  @Override
  public long countNewMessages(Long senderId, Long recipientId) {
    return repository.countBySenderIdAndRecipientIdAndStatus(
        senderId, recipientId, MessageStatus.RECEIVED);
  }

  @Override
  public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
    var chatId = chatRoomService.getChatId(senderId, recipientId, false);
    var messages = chatId.map(repository::findByChatId).orElse(Collections.emptyList());
    if (!messages.isEmpty())
      updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
    return messages;
  }

  @Override
  public ChatMessage findById(Integer id) {
    return repository
        .findById(id)
        .map(chatMessage -> {
          chatMessage.setStatus(MessageStatus.DELIVERED);
          return repository.save(chatMessage);
        }).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND));
  }

  @Override
  public void updateStatuses(Long senderId, Long recipientId, MessageStatus status) {
    repository.updateStatus(senderId, recipientId, status);
  }
}
