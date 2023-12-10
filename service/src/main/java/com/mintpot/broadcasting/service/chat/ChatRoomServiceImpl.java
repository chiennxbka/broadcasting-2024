package com.mintpot.broadcasting.service.chat;

import com.mintpot.broadcasting.common.entities.ChatRoom;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.exception.BusinessException;
import com.mintpot.broadcasting.repository.chat.ChatRoomRepository;
import com.mintpot.broadcasting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository repository;

  private final UserRepository userRepository;

  @Override
  public Optional<String> getChatId(Long senderId, Long recipientId, boolean createIfNotExist) {
    return repository.findBySenderIdAndRecipientId(senderId, recipientId)
        .map(ChatRoom::getChatId)
        .or(() -> {
          if (!createIfNotExist) {
            return Optional.empty();
          }
          var chatId = String.format("%s_%s", senderId, recipientId);

          ChatRoom senderRecipient = ChatRoom
              .builder()
              .chatId(chatId)
              .sender(userRepository.findById(senderId).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND)))
              .recipient(userRepository.findById(recipientId).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND)))
              .build();

          ChatRoom recipientSender = ChatRoom
              .builder()
              .chatId(chatId)
              .sender(userRepository.findById(recipientId).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND)))
              .recipient(userRepository.findById(senderId).orElseThrow(() -> new BusinessException(ErrorCode.OBJECT_NOTFOUND)))
              .build();
          repository.save(senderRecipient);
          repository.save(recipientSender);
          return Optional.of(chatId);
        });
  }
}
