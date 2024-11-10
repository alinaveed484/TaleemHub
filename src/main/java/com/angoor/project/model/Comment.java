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

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;  // Each Comment is linked to a single Post

}
