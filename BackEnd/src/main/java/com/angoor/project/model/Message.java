package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Person sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Person recipient;

    @Column(name = "timestamp")
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type;

    public Integer getMessageId() {
        return messageId;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    // Constructors, Getters, and Setters

    public Message() {}

    public Message(String content, Integer studentID, Integer teacherID, Person sender, LocalDateTime timeStamp, MessageType type) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getRecipient(){
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat)
    {
        this.chat = chat;
    }
}
