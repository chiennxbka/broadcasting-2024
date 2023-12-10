package com.mintpot.broadcasting.repository.chat;

import com.mintpot.broadcasting.common.entities.ChatMessage;
import com.mintpot.broadcasting.common.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

  long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);

  List<ChatMessage> findByChatId(String chatId);

  @Modifying
  @Transactional
  @Query(value = "UPDATE ChatMessage cm set cm.status =:status where cm.sender.id =:senderId and cm.recipient.id =:recipientId")
  void updateStatus(@Param(value = "senderId") Long senderId, @Param(value = "recipientId") Long recipientId, @Param(value = "status") MessageStatus status);
}
