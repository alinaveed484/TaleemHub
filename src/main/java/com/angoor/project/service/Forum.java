package com.angoor.project.service;

import com.angoor.project.model.Comment;
import com.angoor.project.model.Person;
import com.angoor.project.model.Post;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Forum {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public Forum(PostRepo postRepo, CommentRepo commentRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    // Fetch all posts
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    // Create a new post
    public Post createPost(String title, String content, Person person) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreated_at(LocalDateTime.now());
        post.setPerson(person);
        return postRepo.save(post);
    }

    // Add a comment to a post
    public Comment addCommentToPost(Integer postId, String content, Person person) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreated_at(LocalDateTime.now());
        comment.setPost(post);
        comment.setPerson(person);
        return commentRepo.save(comment);
    }

    // Upvote a comment
    public Comment upvoteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setVoteCount(comment.getVoteCount() + 1);  // Increase the vote count by 1
        return commentRepo.save(comment);
    }
}
