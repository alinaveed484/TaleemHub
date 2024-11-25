package com.angoor.project.controller;

import com.angoor.project.model.*;
import com.angoor.project.repository.ChatRepo;
import com.angoor.project.repository.MessageRepo;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.service.ChatHub;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")  // Use a unique path for this controller
public class ChatController {

     private final SimpMessagingTemplate messagingTemplate;
     private final ChatHub chatService;
     private final ChatRepo chatRepo;
     private final MessageRepo messageRepo;
     private final PersonRepo personRepo;

     @Autowired
     public ChatController(SimpMessagingTemplate messagingTemplate, ChatHub chatService, MessageRepo messageRepo, ChatRepo chatRepo, PersonRepo personRepo) {
          this.messagingTemplate = messagingTemplate;
          this.chatService = chatService;
          this.chatRepo = chatRepo;
          this.messageRepo = messageRepo;
          this.personRepo = personRepo;
     }


    @MessageMapping("/send_msg")
    public void processMessage(@Payload MessageDTO messageDTO) {
        // Log the incoming message
        System.out.println("Received MessageDTO: " + messageDTO);

        try {

            // Save the message in the database
            Message savedMessage = chatService.saveMessage(messageDTO);
            System.out.println("Message saved: " + savedMessage);

            // Send chat notification
            ChatNotification chatNotification = new ChatNotification();
            chatNotification.setId(savedMessage.getMessageId());
            chatNotification.setSenderId(messageDTO.getSenderId());
            chatNotification.setRecipientId(messageDTO.getRecipientId());
            chatNotification.setContent(savedMessage.getContent());

            // Notify sender and recipient
            messagingTemplate.convertAndSendToUser(
                    messageDTO.getRecipientId().toString() + "/" + messageDTO.getSenderId().toString(),
                    "/queue/messages",
                    messageDTO
            );

            messagingTemplate.convertAndSendToUser(
                    messageDTO.getSenderId().toString() + "/" + messageDTO.getRecipientId().toString(),
                    "/queue/messages",
                    messageDTO
            );
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<MessageDTO>> findChatMessages(
            @PathVariable("senderId") Integer senderId,
            @PathVariable("recipientId") Integer recipientId) {
        try {
            List<Message> messages = chatService.findMessages(senderId, recipientId);
            List<MessageDTO> messageDTOs = messages.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            System.out.println("Retrieved messages between " + senderId + " and " + recipientId + ": " + messageDTOs);
            return ResponseEntity.ok(messageDTOs);
        } catch (Exception e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // Conversion method to transform Message entity to MessageDTO
    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getMessageId(),
                message.getContent(),
                message.getSender().getId(),
                message.getRecipient().getId(),
                message.getTimeStamp(),
                message.getType().toString(),
                message.getChat().getChatId()
        );
    }


     @GetMapping("/hello2")
     public Map<String, Object> sayHello2() {
          Map<String, Object> response = new HashMap<>();
          response.put("message", "Hello, World!");
          return response;
     }


}
