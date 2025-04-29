package com.angoor.taleemhub;
// src/test/java/com/angoor/project/service/ForumServiceTest.java


import com.angoor.project.model.Comment;
import com.angoor.project.model.Post;
import com.angoor.project.model.Student;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.service.Forum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForumServiceTest {

    @Mock private PostRepo postRepo;
    @Mock private CommentRepo commentRepo;
    @Mock private PersonRepo personRepo;
    @InjectMocks private Forum forumService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createPost should set all fields and save")
    void testCreatePost() {
        Student student = new Student();
        when(postRepo.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

        Post result = forumService.createPost("Ti", "Co", student);

        assertEquals("Ti", result.getTitle());
        assertEquals("Co", result.getContent());
        assertEquals(student, result.getPerson());
        assertNotNull(result.getCreated_at());
        verify(postRepo).save(result);
    }

    @Test
    @DisplayName("addCommentToPost → throws if post not found")
    void testAddCommentToPost_notFound() {
        when(postRepo.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> forumService.addCommentToPost(1, "c", new Student()));
        assertEquals("Post not found", ex.getMessage());
    }

    @Test
    @DisplayName("voteComment increments voteCount but not yet threshold")
    void testVoteComment_noThreshold() {
        Student student = new Student();
        student.setPoints(0);
        Comment comment = new Comment();
        comment.setVoteCount(3);
        comment.setPerson(student);

        when(commentRepo.findById(5)).thenReturn(Optional.of(comment));
        when(commentRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Comment updated = forumService.voteComment(5);

        assertEquals(4, updated.getVoteCount());
        assertEquals(0, student.getPoints());
        verify(personRepo, never()).save(any());
    }

    @Test
    @DisplayName("voteComment at threshold adds points and saves person")
    void testVoteComment_thresholdReached() {
        Student student = new Student();
        student.setPoints(20);
        Comment comment = new Comment();
        comment.setVoteCount(4);  // one away from threshold
        comment.setPerson(student);

        when(commentRepo.findById(7)).thenReturn(Optional.of(comment));
        when(commentRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Comment updated = forumService.voteComment(7);

        assertEquals(5, updated.getVoteCount());
        assertEquals(30, student.getPoints());  // +10 points
        verify(personRepo).save(student);
    }

    @Test
    @DisplayName("getAllPosts just delegates to repository")
    void testGetAllPosts() {
        Post p = new Post();
        when(postRepo.findAll()).thenReturn(List.of(p));

        List<Post> all = forumService.getAllPosts();

        assertEquals(1, all.size());
        assertSame(p, all.get(0));
        verify(postRepo).findAll();
    }
}
