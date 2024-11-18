package com.angoor.taleemhub;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;


import com.angoor.project.model.Chat;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.ChatRepo;
import com.angoor.project.repository.PersonRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ChatRepositoryTest {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private PersonRepo personRepo;

    private Teacher teacher;
    private Student student;
    private Chat existingChat;

    @BeforeEach
    public void setUp() {
        // Create and save teacher and student
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = personRepo.save(teacher);

        student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student = personRepo.save(student);

        // Create and save chat
        existingChat = new Chat();
        existingChat.setTeacher(teacher);
        existingChat.setStudent(student);
        existingChat = chatRepo.save(existingChat);
    }

    @Test
    public void testFindByTeacher_idAndStudent_id_WhenChatExists() {
        // Test case when chat exists
        Optional<Chat> retrievedChat = chatRepo.findByTeacher_idAndStudent_id(teacher.getId(), student.getId());

        assertTrue(retrievedChat.isPresent(), "Expected chat to be found, but it was not.");
        assertEquals(existingChat.getChatId(), retrievedChat.get().getChatId(), "Expected the retrieved chat ID to match the existing chat ID.");
    }

    @Test
    public void testFindByTeacher_idAndStudent_id_WhenChatDoesNotExist() {
        // Test case when no chat exists for given teacher and student IDs
        Optional<Chat> retrievedChat = chatRepo.findByTeacher_idAndStudent_id(999, 888); // Use IDs that don’t exist

        assertFalse(retrievedChat.isPresent(), "Expected no chat to be found, but a chat was retrieved.");
    }
}
