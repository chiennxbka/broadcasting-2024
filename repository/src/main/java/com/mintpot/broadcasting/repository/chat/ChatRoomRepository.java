package com.mintpot.broadcasting.repository.chat;

import com.mintpot.broadcasting.common.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

  Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
