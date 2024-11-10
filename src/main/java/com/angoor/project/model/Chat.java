package com.angoor.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;
    @Column(name = "teacher_id", nullable = false)
    private Integer teacherID;
    @Column(name = "student_id", nullable = false)
    private Integer studentID;

    private List<Message> messages = new ArrayList<>();

    public Chat(Integer teacherID, Integer studentID) {
        this.teacherID = teacherID;
        this.studentID = studentID;
    }

    public Integer getTeacherID() {
        return teacherID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
