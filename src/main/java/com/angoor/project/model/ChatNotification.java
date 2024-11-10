package com.angoor.project.model;

import org.springframework.messaging.handler.annotation.Payload;


public class ChatNotification {
    private Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String content;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public String getContent() {
        return content;
    }
}
