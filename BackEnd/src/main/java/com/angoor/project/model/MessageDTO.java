package com.angoor.project.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MessageDTO {

    private Integer messageId;
    private String content;
    private Integer senderId;
    private Integer recipientId;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Define the format
    @JsonProperty("timeStamp")
    private LocalDateTime timeStamp;

    private String type;
    private Integer chatId;

    // Default constructor
    public MessageDTO() {}

    // Constructor with fields
    public MessageDTO(Integer messageId, String content, Integer senderId, Integer recipientId, LocalDateTime timeStamp, String type, Integer chatId) {
        this.messageId = messageId;
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.timeStamp = timeStamp;
        this.type = type;
        this.chatId = chatId;
    }

    // Getters and Setters
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
