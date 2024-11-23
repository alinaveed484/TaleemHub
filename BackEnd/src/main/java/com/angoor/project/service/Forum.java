package com.angoor.project.service;

import com.angoor.project.model.Comment;
import com.angoor.project.model.Person;
import com.angoor.project.model.Post;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class Forum {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    private static final int VOTE_THRESHOLD = 5; // Every 5 upvotes gives points
    private static final int POINTS_PER_THRESHOLD = 10;
    private final PersonRepo personRepo;

    @Autowired
    public Forum(PostRepo postRepo, CommentRepo commentRepo, PersonRepo personRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.personRepo = personRepo;
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
        post.setCreated_at(LocalDate.now());
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

    //This will increase the Votes and the Points of the Person
    public Comment voteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
        Person person = comment.getPerson();

        // Increase vote count for upvote
        comment.setVoteCount(comment.getVoteCount() + 1);

        // Check if vote count has reached the threshold to add points
        if (comment.getVoteCount() % VOTE_THRESHOLD == 0) {
            person.setPoints(person.getPoints() + POINTS_PER_THRESHOLD);
            personRepo.save(person); // Save the updated points for the user
        }

        // Save the updated comment
        return commentRepo.save(comment);
    }
}
