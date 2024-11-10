package com.angoor.project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;
    private String profilePhotoURL;


    private boolean status = false;

    // One Person as a Teacher can have multiple Chat instances
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsTeacher;

    // One Person as a Student can have multiple Chat instances
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsStudent;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messagesReceived;


    @OneToMany(mappedBy = "person")
    private List<Post> posts;  // A person can have multiple posts

    @OneToMany(mappedBy = "person")
    private List<Comment> comments;  // A person can have multiple comments

    public void setId(Integer id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }
    public Integer getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public List<Chat> getChatsAsTeacher() {
        return chatsAsTeacher;
    }
    public void setChatsAsTeacher(List<Chat> chatsAsTeacher) {
        this.chatsAsTeacher = chatsAsTeacher;
    }
    public List<Chat> getChatsAsStudent() {
        return chatsAsStudent;
    }
    public void setChatsAsStudent(List<Chat> chatsAsStudent) {
        this.chatsAsStudent = chatsAsStudent;
    }
    public List<Message> getMessagesSent() {
        return messagesSent;
    }
    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }
    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }
}
