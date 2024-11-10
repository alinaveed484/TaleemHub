package com.angoor.taleemhub;

import com.angoor.project.controller.ChatController;
import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import com.angoor.project.model.MessageType;
import com.angoor.project.model.Person;
import com.angoor.project.service.ChatHub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChatHub chatService;

    @InjectMocks
    private ChatController chatController;

    private Message message;
    private Person teacher;
    private Person student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample teacher and student
        teacher = new Person() {{ setId(1); setFirstName("John"); setLastName("Doe"); }};
        student = new Person() {{ setId(2); setFirstName("Jane"); setLastName("Smith"); }};

        // Create a sample message
        message = new Message();
        message.setContent("Hello, this is a test message.");
        message.setSender(teacher);
        message.setTimeStamp(LocalDateTime.parse("2023-04-10T15:30:00"));
        message.setType(MessageType.CHAT);

        // Set up chat association
        Chat chat = new Chat(teacher, student);
        message.setChat(chat);
    }

    @Test
    void testSendMessage() {
        // Mock the ChatHub service to return the chat
        Chat chat = message.getChat();
        when(chatService.saveMessage(message)).thenReturn(chat);

        // Call the sendMessage method
        chatController.sendMessage(message);

        // Capture the destination and message passed to messagingTemplate.convertAndSend
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messagingTemplate).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());

        // Verify the destination and message content
        String expectedDestination = "/topic/chat/t" + teacher.getId() + "-s" + student.getId();
        assertEquals(expectedDestination, destinationCaptor.getValue());
        assertEquals("Hello, this is a test message.", messageCaptor.getValue().getContent());
    }
}
