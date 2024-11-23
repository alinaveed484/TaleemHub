package com.angoor.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommentReturnDto {
    private String content;
    private LocalDateTime created_at;
    private String author;

    public CommentReturnDto(String content, LocalDateTime created_at, String author) {
        this.content = content;
        this.created_at = created_at;
        this.author = author;
    }
    public CommentReturnDto() {}

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public String getAuthor() {
        return author;
    }
}
