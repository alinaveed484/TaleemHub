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

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Person teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Person student;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Chat(Person teacher, Person student) {
        this.teacher = teacher;
        this.student = student;
    }

    public Person getTeacher() {
        return teacher;
    }

    public Person getStudent() {
        return student;
    }



}
