package com.angoor.taleemhub;

import com.angoor.project.model.Student;
import com.angoor.project.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaleemHubApplicationTests {

    @Autowired
    private MockMvc mockMvc;  // Inject MockMvc for HTTP testing

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void testSayHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))  // Perform a GET request to /hello
                .andExpect(MockMvcResultMatchers.status().isOk())  // Expect HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello, World!"));  // Check "status" valu
    }

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testCreateStudent() {
        // Create a new student object
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGradeLevel(10);
        student.setEmail("john.doe@example.com");
        student.setPhone("1234567890");

        // Save the student to the database
        Student savedStudent = studentRepository.save(student);

        // Verify the student is saved
        assertNotNull(savedStudent.getStudentId());  // Ensure the ID is generated
        assertEquals("John", savedStudent.getFirstName());
        assertEquals("Doe", savedStudent.getLastName());
    }

}
