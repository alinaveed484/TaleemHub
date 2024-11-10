package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "student_id")
    private Integer studentID;

    @Column(name = "teacher_id")
    private Integer teacherID;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "timestamp")
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type;

    // Constructors, Getters, and Setters

    public Message() {}

    public Message(String content, Integer studentID, Integer teacherID, String sender, LocalDateTime timeStamp, MessageType type) {
        this.content = content;
        this.studentID = studentID;
        this.teacherID = teacherID;
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

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public Integer getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(Integer teacherID) {
        this.teacherID = teacherID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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



}
