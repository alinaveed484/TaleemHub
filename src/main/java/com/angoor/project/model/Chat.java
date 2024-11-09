package com.angoor.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


public class Chat {

    private Integer teacherID;
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
