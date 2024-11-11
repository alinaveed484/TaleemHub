package com.angoor.project.controller;

import com.angoor.project.dto.CommentDto;
import com.angoor.project.dto.PostDto;
import com.angoor.project.model.Comment;
import com.angoor.project.model.Person;
import com.angoor.project.model.Post;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final Forum forumService;
    private final PersonRepo personRepo;

    @Autowired
    public ForumController(Forum forumService, PersonRepo personRepo) {
        this.forumService = forumService;
        this.personRepo = personRepo;
    }

    // Endpoint to fetch all posts
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = forumService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Endpoint to create a new post
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody PostDto postDto) {
        Person person = personRepo.findById(postDto.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        Post post = forumService.createPost(postDto.getTitle(), postDto.getContent(), person);
        return ResponseEntity.ok(post);
    }

    // Endpoint to add a comment to a post
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Integer postId, @RequestBody CommentDto commentDto) {
        Person person = personRepo.findById(commentDto.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        Comment comment = forumService.addCommentToPost(postId, commentDto.getContent(), person);
        return ResponseEntity.ok(comment);
    }

    // Endpoint to upvote a comment
    @PostMapping("/comments/{commentId}/upvote")
    public ResponseEntity<Comment> upvoteComment(@PathVariable Integer commentId) {
        Comment updatedComment = forumService.upvoteComment(commentId);
        return ResponseEntity.ok(updatedComment);
    }
}