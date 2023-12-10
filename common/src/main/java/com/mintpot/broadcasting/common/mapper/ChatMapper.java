package com.mintpot.broadcasting.common.mapper;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.request.ChatMessageRequest;
import org.mapstruct.Mapper;

@Mapper
public interface ChatMapper {
  default ChatMessage toEntity(ChatMessageRequest request) {
    return null;
  }
}
