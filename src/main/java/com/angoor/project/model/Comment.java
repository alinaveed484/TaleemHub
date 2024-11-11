package com.angoor.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;
    private String content;
    private LocalDateTime created_at;
    private Integer voteCount = 0;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // Each Comment is linked to a single Post

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;  // Each Comment is linked to a Person (Student/Teacher)

    public void setPerson(Person person) {
        this.person = person;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
    public Integer getVoteCount() {
        return voteCount;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
