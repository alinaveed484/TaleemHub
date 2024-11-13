package com.angoor.taleemhub;

import com.angoor.project.controller.ChatController;
import com.angoor.project.model.*;
import com.angoor.project.service.ChatHub;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChatHub chatService;

    @InjectMocks
    private ChatController chatController;

    private Message message;
    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample teacher with additional details
        teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("johndoe@example.com");
        teacher.setPhone("123-456-7890");
        teacher.setProfilePhotoURL("https://example.com/photos/john");
        teacher.setYearsExperience(10);
        teacher.setHireDate(LocalDate.parse("2015-08-01"));
        teacher.setSubjectSpecialization("Mathematics");

        // Create a sample student with additional details
        student = new Student();
        student.setId(2);
        student.setFirstName("Jane");
        student.setLastName("Smith");
        student.setEmail("janesmith@example.com");
        student.setPhone("987-654-3210");
        student.setProfilePhotoURL("https://example.com/photos/jane");
        student.setGradeLevel(10);
        student.setEnrollmentDate(LocalDate.parse("2020-09-01"));

        // Create a sample chat between the teacher and student
        Chat chat = new Chat(teacher, student);

        // Create a sample message and associate it with the chat
        message = new Message();
        message.setContent("Hello, this is a test message.");
        message.setSender(teacher);  // Setting the sender as the teacher
        message.setTimeStamp(LocalDateTime.parse("2023-04-10T15:30:00"));
        message.setType(MessageType.CHAT);
        message.setChat(chat);
    }

//    @Test
//    void testSendMessage() {
//        // Mock the ChatHub service to return the chat
//        Chat chat = message.getChat();
//        when(chatService.saveMessage(message)).thenReturn(chat);
//
//        // Call the sendMessage method
//        chatController.sendMessage(message);
//
//        // Capture the destination and message passed to messagingTemplate.convertAndSend
//        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
//        verify(messagingTemplate).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());
//
//        // Verify the destination and message content
//        String expectedDestination = "/topic/chat/t" + teacher.getId() + "-s" + student.getId();
//        assertEquals(expectedDestination, destinationCaptor.getValue());
//        assertEquals("Hello, this is a test message.", messageCaptor.getValue().getContent());
//    }
}
