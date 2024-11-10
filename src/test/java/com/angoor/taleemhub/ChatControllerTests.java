package com.angoor.taleemhub;

import com.angoor.project.controller.ChatController;
import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import com.angoor.project.model.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatController chatController;

    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample message
        message = new Message();
        message.setContent("Hello, this is a test message.");
//        message.setStudentID(2);
//        message.setTeacherID(1);
//        message.setSender("teacher");
//        message.setTimeStamp("2023-04-10T15:30:00");
//        message.setType(MessageType.CHAT);
    }

    @Test
    void testSendMessage() {
        // Call the sendMessage method
        chatController.sendMessage(message);

        // Verify the message was added to chatHistory
//        String chatKey = "t1-s2";
//        Map<String, Chat> chatHistory = chatController.getChatHistory();
//        Chat chat = chatHistory.get(chatKey);
//        assertEquals(1, chat.getMessages().size());
//        assertEquals("Hello, this is a test message.", chat.getMessages().get(0).getContent());
//
//        // Capture the arguments passed to messagingTemplate.convertAndSend
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messagingTemplate).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());

        // Verify the destination and message content
        assertEquals("/topic/chat/t1-s2", destinationCaptor.getValue());
        assertEquals("Hello, this is a test message.", messageCaptor.getValue().getContent());
    }
}
