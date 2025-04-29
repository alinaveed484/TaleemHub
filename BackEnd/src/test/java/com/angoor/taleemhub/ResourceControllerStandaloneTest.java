package com.angoor.taleemhub;

import com.angoor.project.controller.TaleemHub;
import com.angoor.project.model.Resource;
import com.angoor.project.model.Student;
import com.angoor.project.model.resource_category;
import com.angoor.project.model.resource_subject;
import com.angoor.project.service.ResourceHub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResourceControllerStandaloneTest {

    @Mock
    private ResourceHub resourceService;

    @InjectMocks
    private TaleemHub controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Standalone MVC with a no-op view resolver and JSON converter
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/noop"))
                .build();
    }

    @Test
    @DisplayName("GET /resource/share_resource/get_resource_categories → JSON map")
    void getResourceCategories_returnsJson() throws Exception {
        Map<String,Object> cats = Map.of(
                "1","Text",
                "2","Image",
                "3","Document",
                "4","Video"
        );
        when(resourceService.getResourceCategories()).thenReturn(cats);

        mockMvc.perform(get("/resource/share_resource/get_resource_categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.1").value("Text"))
                .andExpect(jsonPath("$.4").value("Video"));
    }

    @Test
    @DisplayName("GET /resource/upload → resource-upload view + enums")
    void showUploadForm_rendersView() throws Exception {
        mockMvc.perform(get("/resource/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("resource-upload"))
                .andExpect(model().attribute("subjects", resource_subject.values()))
                .andExpect(model().attribute("categories", resource_category.values()));
    }

    @Test
    @DisplayName("POST /resource/upload → 200 + success text")
    void shareResource_uploadsAndReturnsOk() throws Exception {
        // Prepare a dummy file
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "hello".getBytes()
        );

        // Stub service – return any ResponseEntity; controller ignores it
        when(resourceService.uploadResource(
                any(), eq("T"), eq(resource_category.Document),
                eq("u1"), eq(resource_subject.Physics), eq("descr")
        )).thenReturn(ResponseEntity.ok("ignored"));

        mockMvc.perform(multipart("/resource/upload")
                        .file(file)
                        .param("title", "T")
                        .param("category", "Document")
                        .param("uploader_id", "u1")
                        .param("subject", "Physics")
                        .param("description", "descr")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Resource uploaded successfully"));
    }

    @Test
    @DisplayName("GET /resources/view → resources-view with all resources")
    void viewResources_withoutSubject() throws Exception {
        // build a dummy Resource
        Student uploader = new Student();
        Resource r = new Resource(1, "R1", uploader, resource_category.Text, "/dummy");
        when(resourceService.getAllResources()).thenReturn(List.of(r));

        mockMvc.perform(get("/resources/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources-view"))
                .andExpect(model().attribute("resources", hasSize(1)))
                .andExpect(model().attribute("subjects", resource_subject.values()));
    }

    @Test
    @DisplayName("GET /resources/view?subject=Chemistry → filtered list")
    void viewResources_withSubject() throws Exception {
        Student uploader = new Student();
        Resource r = new Resource(2, "R2", uploader, resource_category.Image, "/dummy2");
        when(resourceService.getResourcesBySubject(resource_subject.Chemistry))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/resources/view")
                        .param("subject", "Chemistry"))
                .andExpect(status().isOk())
                .andExpect(view().name("resources-view"))
                .andExpect(model().attribute("resources", hasSize(1)))
                .andExpect(model().attribute("subjects", resource_subject.values()));
    }

    @Test
    @DisplayName("GET /resources/download/{id} → 404 if not found or file missing")
    void downloadFile_notFound() throws Exception {
        // case A: service returns null
        when(resourceService.getResourceById(1)).thenReturn(null);
        mockMvc.perform(get("/resources/download/1"))
                .andExpect(status().isNotFound());

        // case B: service returns a Resource but file path doesn't exist
        Student uploader = new Student();
        Resource r2 = new Resource(2, "R2", uploader, resource_category.Video, "/no/such/file");
        when(resourceService.getResourceById(2)).thenReturn(r2);
        mockMvc.perform(get("/resources/download/2"))
                .andExpect(status().isNotFound());
    }
}
