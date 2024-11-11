package com.angoor.project.dto;

public class CommentDto {
    private String content;
    private Integer personId;  // Use personId instead of the full Person object

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
}
