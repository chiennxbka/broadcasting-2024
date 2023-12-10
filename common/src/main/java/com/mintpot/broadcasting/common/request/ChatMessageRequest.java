package com.mintpot.broadcasting.common.request;

import lombok.Getter;

import java.util.Date;

@Getter
public class ChatMessageRequest {

    private int id;

    private String chatId;

    private Long senderId;

    private Long recipientId;

    private String content;

    private Date sendDate;
}
