package com.mintpot.broadcasting.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatNotification {
  private int id;
  private Long senderId;
}
