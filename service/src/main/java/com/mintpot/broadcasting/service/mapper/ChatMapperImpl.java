package com.mintpot.broadcasting.service.mapper;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.mapper.ChatMapper;
import com.mintpot.broadcasting.common.request.ChatMessageRequest;
import com.mintpot.broadcasting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMapperImpl implements ChatMapper {

  private final UserRepository userRepository;

  @Override
  public ChatMessage toEntity(ChatMessageRequest request) {
    return ChatMessage.builder()
        .content(request.getContent())
        .sender(userRepository.getOne(request.getSenderId()))
        .recipient(userRepository.getOne(request.getRecipientId()))
        .sendDate(request.getSendDate())
        .build();
  }
}
