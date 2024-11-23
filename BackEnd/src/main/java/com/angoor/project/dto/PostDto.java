package com.angoor.project.dto;


import java.time.LocalDateTime;

public class PostDto {
    private String title;
    private String content;
    private String uid;  // Use personId instead of the full Person object
    private LocalDateTime created_at;

    public PostDto(){}

    public PostDto(String title, String content, String uid) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.created_at = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
