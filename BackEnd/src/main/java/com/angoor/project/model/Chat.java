package com.angoor.project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;

    public void setStudent(Person student) {
        this.student = student;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private Person teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Person student;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Chat() {
    }

    public Chat(Person teacher, Person student) {
        this.teacher = teacher;
        this.student = student;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Person getTeacher() {
        return teacher;
    }

    public Person getStudent() {
        return student;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


}
