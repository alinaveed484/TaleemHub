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
        assertNotNull(savedStudent.getStudentId());  // Ensure the ID is generated
        assertEquals("John", savedStudent.getFirstName());
        assertEquals("Doe", savedStudent.getLastName());
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
        Student fetchedStudent = studentRepository.findById(student.getStudentId()).orElseThrow();

        // Assert the teacher is added correctly
        assertTrue(fetchedStudent.getTeachers().contains(teacher), "Teacher should be linked to the student");

        // Assert the student is linked back to the teacher
        Teacher fetchedTeacher = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();
        assertTrue(fetchedTeacher.getStudents().contains(fetchedStudent), "Student should be linked to the teacher");
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

        // Add teacher to student
        student.getTeacherRequests().add(teacher);  // Add teacher to student's teachers set
        teacher.getStudentRequests().add(student);

        // Save student (this will also persist the relationship)
        student = studentRepository.save(student);
        teacher = teacherRepository.save(teacher);

        // Reload student from the database to check relationships
        Student fetchedStudent = studentRepository.findById(student.getStudentId()).orElseThrow();
        Teacher fetchedTeacher = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();

        // Assert the teacher is added correctly
        assertTrue(fetchedStudent.getTeacherRequests().contains(fetchedTeacher), "Teacher should be linked to the student");

        // Assert the student is linked back to the teacher

        assertTrue(fetchedTeacher.getStudentRequests().contains(fetchedStudent), "Student should be linked to the teacher");
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void testSelectMentorEndpoint() throws Exception {
        mockMvc.perform(get("/student/select_mentor/send_mentor_request?teacherID=1&studentID=1"))  // Perform a GET request to /hello
                .andExpect(MockMvcResultMatchers.status().isOk());// Check "status" valu
    }

}
