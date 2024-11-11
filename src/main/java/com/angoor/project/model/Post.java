package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String content;
    private LocalDateTime created_at;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;  // One Post can have multiple comments

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;  // Each Post is linked to a Person (Student/Teacher)

    public void setPerson(Person person) {
        this.person = person;
    }
}
