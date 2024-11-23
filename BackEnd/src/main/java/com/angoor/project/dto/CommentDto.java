package com.angoor.project.dto;

public class CommentDto {
    private String content;
    private String uid;  // Use personId instead of the full Person object

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
