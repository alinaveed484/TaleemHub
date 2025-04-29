package com.angoor.taleemhub;

import com.angoor.project.model.Person;
import com.angoor.project.model.Resource;
import com.angoor.project.model.resource_category;
import com.angoor.project.model.resource_subject;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.ResourceRepo;
import com.angoor.project.service.ResourceHub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceHubTest {

    @Mock private ResourceRepo resourceRepo;
    @Mock private PersonRepo personRepo;

    // use a temp directory for storagePath; no real files are created in these tests
    private ResourceHub resourceHub;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        resourceHub = new ResourceHub(resourceRepo, personRepo, "/tmp");
    }

    @Test
    @DisplayName("getResourceCategories returns the four entries")
    void getResourceCategories() {
        Map<String,Object> cats = resourceHub.getResourceCategories();
        assertEquals(4, cats.size());
        assertEquals("Text", cats.get("1"));
        assertEquals("Video", cats.get("4"));
    }

    @Test
    @DisplayName("getAllResources delegates to repository")
    void getAllResources() {
        Resource r1 = new Resource();
        Resource r2 = new Resource();
        when(resourceRepo.findAll()).thenReturn(List.of(r1, r2));
        var out = resourceHub.getAllResources();
        assertEquals(2, out.size());
        verify(resourceRepo).findAll();
    }

    @Test
    @DisplayName("getResourcesBySubject delegates correctly")
    void getResourcesBySubject() {
        Resource r = new Resource();
        when(resourceRepo.findBySubject(resource_subject.Maths))
                .thenReturn(List.of(r));
        var out = resourceHub.getResourcesBySubject(resource_subject.Maths);
        assertEquals(1, out.size());
        verify(resourceRepo).findBySubject(resource_subject.Maths);
    }

    @Test
    @DisplayName("uploadResource returns 404 when uploader not found")
    void uploadResource_personNotFound() throws Exception {
        // prepare a dummy file
        MockMultipartFile file = new MockMultipartFile(
                "file", "foo.txt",
                "text/plain", "data".getBytes()
        );

        when(personRepo.findByUid("unknown")).thenReturn(Optional.empty());

        ResponseEntity<String> resp = resourceHub.uploadResource(
                file, "T", resource_category.Document,
                "unknown", resource_subject.History, "descr"
        );
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Person ID not found"));
    }

    @Test
    @DisplayName("getResourceById returns resource or null")
    void getResourceById() {
        Resource r = new Resource();
        when(resourceRepo.findById(5)).thenReturn(Optional.of(r));
        assertSame(r, resourceHub.getResourceById(5));

        when(resourceRepo.findById(6)).thenReturn(Optional.empty());
        assertNull(resourceHub.getResourceById(6));
    }
}
