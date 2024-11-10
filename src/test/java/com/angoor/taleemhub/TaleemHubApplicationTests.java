package com.angoor.taleemhub;

import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    private TeacherRepository teacherRepository;

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
        assertNotNull(savedStudent.getId());  // Ensure the ID is generated

        // Verify that the student fields are correctly saved
        assertEquals("John", savedStudent.getFirstName());
        assertEquals("Doe", savedStudent.getLastName());
        assertEquals(10, savedStudent.getGradeLevel());
        assertEquals("john.doe@example.com", savedStudent.getEmail());
        assertEquals("1234567890", savedStudent.getPhone());

        // If student has associations with other entities (e.g., teachers or mentor requests), verify them
        assertTrue(savedStudent.getTeachers().isEmpty()); // If teachers list is initially empty

        // Optionally, verify the relationship with the Person class if required.
        assertEquals("John", savedStudent.getFirstName());  // Check for the parent class' firstName (if Person is inherited)
    }


    @Test
    public void testManyToManyRelationship() {
        // Create a teacher
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setSubjectSpecialization("Math");
        teacher = teacherRepository.save(teacher);

        // Create a student
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setEmail("alice.smith@example.com");

        // Add teacher to student
        student.getTeachers().add(teacher);  // Add teacher to student's teachers set
        teacher.getStudents().add(student);

        // Save student (this will also persist the relationship)
        student = studentRepository.save(student);

        // Reload student from the database to check relationships
        Student fetchedStudent = studentRepository.findById(student.getId()).orElseThrow();

        // Assert that the student was saved correctly
        assertEquals("Alice", fetchedStudent.getFirstName());
        assertEquals("Smith", fetchedStudent.getLastName());
        assertEquals("alice.smith@example.com", fetchedStudent.getEmail());

        // Assert the teacher is added correctly to the student's teachers list
        assertTrue(fetchedStudent.getTeachers().contains(teacher), "Teacher should be linked to the student");

        // Reload teacher from the database
        Teacher fetchedTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();

        // Assert the teacher's students list contains the student
        assertTrue(fetchedTeacher.getStudents().contains(fetchedStudent), "Student should be linked to the teacher");

        // Optionally, you could also assert that the teacher details are correct:
        assertEquals("John", fetchedTeacher.getFirstName());
        assertEquals("Doe", fetchedTeacher.getLastName());
        assertEquals("Math", fetchedTeacher.getSubjectSpecialization());
    }


    @Test
    public void testManyToManyRelationshipRequests() {
        // Create a teacher
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setSubjectSpecialization("Math");

        // Create a student
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setEmail("alice.smith@example.com");

        // Add teacher to student requests
        student.getTeacherRequests().add(teacher);  // Add teacher to student's teacherRequests set
        teacher.getStudentRequests().add(student);  // Add student to teacher's studentRequests set

        // Save student and teacher (this will persist the relationship)
        student = studentRepository.save(student);
        teacher = teacherRepository.save(teacher);

        // Reload student from the database to check relationships
        Student fetchedStudent = studentRepository.findById(student.getId()).orElseThrow();
        Teacher fetchedTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();

        // Assert the teacher is added correctly to student requests
        assertTrue(fetchedStudent.getTeacherRequests().contains(fetchedTeacher), "Teacher should be linked to the student");

        // Assert the student is linked back to the teacher's requests
        assertTrue(fetchedTeacher.getStudentRequests().contains(fetchedStudent), "Student should be linked to the teacher");
    }


    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void testSelectMentorEndpoint() throws Exception {
        mockMvc.perform(get("/student/select_mentor/send_mentor_request?teacherID=1&studentID=1"))  // Perform a GET request to /hello
                .andExpect(MockMvcResultMatchers.status().isOk());// Check "status" valu
    }

}
