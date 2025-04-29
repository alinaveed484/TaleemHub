package com.angoor.taleemhub;

import com.angoor.project.controller.ForumController;
import com.angoor.project.dto.postreturnDto;
import com.angoor.project.model.Comment;
import com.angoor.project.model.Post;
import com.angoor.project.model.Student;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.Forum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ForumControllerStandaloneTest {

    @Mock
    private Forum forumService;

    @Mock
    private PersonRepo personRepo;

    @InjectMocks
    private ForumController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                // if you use view names and want a no-op view resolver:
                .setSingleView(new org.springframework.web.servlet.view.InternalResourceView("/noop"))
                .build();
    }

    @Test
    @DisplayName("GET /api/forum/posts → renders forum list and populates DTO correctly")
    void getAllPosts_rendersForumList() throws Exception {
        // prepare a Student
        Student student = new Student();
        student.setUid("u1");
        student.setFirstName("Alice");
        student.setLastName("Smith");

        // prepare a Post with reflection for private fields
        Post post = new Post();
        Field idField = Post.class.getDeclaredField("postId");
        idField.setAccessible(true);
        idField.set(post, 42);
        post.setTitle("Hello");
        post.setContent("World");
        post.setCreated_at(LocalDate.of(2025, 4, 1));
        post.setPerson(student);
        Field commentsField = Post.class.getDeclaredField("comments");
        commentsField.setAccessible(true);
        commentsField.set(post, List.of());

        when(forumService.getAllPosts()).thenReturn(List.of(post));

        // perform and grab the ModelAndView
        var mvcResult = mockMvc.perform(get("/api/forum/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("bs5_forum_list"))
                .andReturn();

        @SuppressWarnings("unchecked")
        List<Object> rawPosts = (List<Object>) mvcResult
                .getModelAndView().getModel().get("posts");
        assertEquals(1, rawPosts.size(), "Should have exactly one DTO");

        // cast to your DTO type
        postreturnDto dto = (postreturnDto) rawPosts.get(0);

        // now assert on its getters directly
        assertEquals(42, dto.getId());
        assertEquals("Hello", dto.getTitle());
        assertEquals("Alice Smith", dto.getPersonName());
        assertTrue(dto.getComments().isEmpty());
    }


    @Test
    @DisplayName("POST /api/forum/posts → 200 + success JSON")
    void createPost_returnsSuccess() throws Exception {
        var student = new Student();
        student.setUid("u1");

        when(personRepo.findByUid("u1")).thenReturn(Optional.of(student));
        when(forumService.createPost(eq("T"), eq("C"), any())).thenReturn(new Post());

        mockMvc.perform(post("/api/forum/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  { "uid":"u1", "title":"T", "content":"C" }
                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Post created successfully"));
    }

    @Test
    @DisplayName("POST /api/forum/posts/{postId}/comments → 200 + success JSON")
    void addCommentToPost_returnsSuccess() throws Exception {
        var student = new Student();
        student.setUid("u1");

        when(personRepo.findByUid("u1")).thenReturn(Optional.of(student));
        when(forumService.addCommentToPost(eq(42), eq("Nice!"), any()))
                .thenReturn(new Comment());

        mockMvc.perform(post("/api/forum/posts/42/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  { "uid":"u1", "content":"Nice!" }
                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Post created successfully"));
    }

    @Test
    @DisplayName("POST /api/forum/comments/{commentId}/upvote → 200 + success JSON")
    void upvoteComment_returnsSuccess() throws Exception {
        when(forumService.voteComment(99)).thenReturn(new Comment());

        mockMvc.perform(post("/api/forum/comments/99/upvote"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Post created successfully"));
    }
}
