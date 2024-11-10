package com.angoor.project.controller;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/app")  // Use a unique path for this controller
public class ChatController {

     private final SimpMessagingTemplate messagingTemplate;

     public ChatController(SimpMessagingTemplate messagingTemplate) {
          this.messagingTemplate = messagingTemplate;
     }

     private Map<String, Chat> chatHistory = new HashMap<>();

     @MessageMapping({"/teacher/answer_student/chat.sendMessage", "/student/question_teacher/chat.sendMessage"})
     public void sendMessage(@Payload Message message){

          String chatKey = generateChatKey(message.getTeacherID(), message.getStudentID());

          Chat chat = chatHistory.computeIfAbsent(chatKey, k -> new Chat(message.getTeacherID(), message.getStudentID()));

          chat.addMessage(message);

          String destination = "/topic/chat/" + chatKey;

          messagingTemplate.convertAndSend(destination, message);

     }

     private String generateChatKey(Integer teacherID, Integer studentID) {
          return "t" + teacherID.toString() + "-" + "s" + studentID.toString();
     }

     public Map<String, Chat> getChatHistory() {
          return chatHistory;
     }

     @GetMapping("/hello2")
     public Map<String, Object> sayHelloHello() {
          Map<String, Object> response = new HashMap<>();
          response.put("message", "Hello, World!");
          return response;
     }


}
