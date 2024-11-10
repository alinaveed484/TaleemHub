package com.angoor.project.controller;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import com.angoor.project.repository.ChatRepo;
import com.angoor.project.repository.MessageRepo;
import com.angoor.project.service.ChatHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")  // Use a unique path for this controller
public class ChatController {

     private final SimpMessagingTemplate messagingTemplate;
     private final ChatHub chatService;
     private final ChatRepo chatRepo;
     private final MessageRepo messageRepo;

     @Autowired
     public ChatController(SimpMessagingTemplate messagingTemplate, ChatHub chatService, MessageRepo messageRepo, ChatRepo chatRepo) {
          this.messagingTemplate = messagingTemplate;
          this.chatService = chatService;
          this.chatRepo = chatRepo;
          this.messageRepo = messageRepo;
     }

     @MessageMapping({"/chat/teacher/answer_student/chat.sendMessage", "/chat/student/question_teacher/chat.sendMessage"})
     public void sendMessage(@Payload Message message){

          String chatKey = generateChatKey(message.getChat().getTeacher().getId(), message.getChat().getStudent().getId());

          Chat chat = chatService.saveMessage(message);

          String destination = "/topic/chat/" + chatKey;

          messagingTemplate.convertAndSend(destination, message);

     }

     private String generateChatKey(Integer teacherID, Integer studentID) {
          return "t" + teacherID.toString() + "-" + "s" + studentID.toString();
     }


     @GetMapping("/hello2")
     public Map<String, Object> sayHello2() {
          Map<String, Object> response = new HashMap<>();
          response.put("message", "Hello, World!");
          return response;
     }


}
