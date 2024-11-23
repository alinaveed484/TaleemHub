package com.angoor.project.dto;

import com.angoor.project.model.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class postreturnDto {
    private Integer id;
    private String title;
    private String content;
    private String uid;  // Use personId instead of the full Person object
    private LocalDate created_at;
    private String personName;
    private List<CommentReturnDto> comments;

    public postreturnDto(Integer id, String title, String content, String uid, LocalDate created_at, String personName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.created_at = created_at;
        this.personName = personName;
        comments = new ArrayList<>();
    }

    public void addComment(List<Comment> postcomments) {

//        String content, LocalDateTime created_at, String Author
        for(Comment comment : postcomments) {
            comments.add(new CommentReturnDto(comment.getContent(), comment.getCreated_at(), comment.getPerson().getFirstName()));
        }

    }

    public void setComments(List<CommentReturnDto> comments) {
        this.comments = comments;
    }

    public List<CommentReturnDto> getComments() {
        return comments;
    }

    public postreturnDto(){

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }
}
