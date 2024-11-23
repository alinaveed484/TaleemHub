package com.angoor.project.controller;

import com.angoor.project.dto.CommentDto;
import com.angoor.project.dto.PostDto;
import com.angoor.project.dto.postreturnDto;
import com.angoor.project.model.Comment;
import com.angoor.project.model.Person;
import com.angoor.project.model.Post;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
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
    public String getAllPosts(Model model){
        System.out.println("Hello World!");
        List<Post> posts = forumService.getAllPosts();

        // Manually map Post to PostDto
        List<postreturnDto> postDtos = posts.stream()
                .map(post -> new postreturnDto(post.getPostId(), post.getTitle(), post.getContent(), post.getPerson().getUid(), post.getCreated_at(), (post.getPerson().getFirstName()+ " " +post.getPerson().getLastName())  ))
                .toList();

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            postreturnDto postDto = postDtos.get(i);
            postDto.addComment(post.getComments());
        }

        model.addAttribute("posts", postDtos);
        return "bs5_forum_list";
    }

    // Endpoint to create a new post
    @ResponseBody
    @PostMapping("/posts")
    public ResponseEntity<Map<String,String>> createPost(@RequestBody PostDto postDto) {
        System.out.println("UID received: " + postDto.getUid());
        Person person = personRepo.findByUid(postDto.getUid())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        Post post = forumService.createPost(postDto.getTitle(), postDto.getContent(), person);

        // Creating a response with a message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post created successfully");
        return ResponseEntity.ok(response);
    }

    // Endpoint to add a comment to a post
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String,String>> addCommentToPost(@PathVariable Integer postId, @RequestBody CommentDto commentDto) {
        Person person = personRepo.findByUid(commentDto.getUid())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        Comment comment = forumService.addCommentToPost(postId, commentDto.getContent(), person);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post created successfully");
        return ResponseEntity.ok(response);
    }

    // Endpoint to vote on a comment (upvote or downvote)
    @PostMapping("/comments/{commentId}/vote")
    public ResponseEntity<Comment> voteComment(@PathVariable Integer commentId,
                                               @RequestParam("voteType") String voteType) {
        Comment updatedComment = forumService.voteComment(commentId, voteType);
        return ResponseEntity.ok(updatedComment);
    }

}