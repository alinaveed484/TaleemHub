package com.angoor.project.service;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import com.angoor.project.model.Person;
import com.angoor.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ChatHub {

    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final PersonRepo personRepo;

    @Autowired
    public ChatHub(ChatRepo chatRepo, MessageRepo messageRepo, PersonRepo personRepo) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
        this.personRepo = personRepo;
    }



    public Message saveMessage(Message message) {
        // Retrieve or create the Chat
        Chat chat = getOrCreateChat(message.getChat().getTeacher(), message.getChat().getStudent());

        // Set the Chat in the Message entity
        message.setChat(chat);

        // Save the message in the repository
        messageRepo.save(message);

        return message;
    }

    private Chat getOrCreateChat(Person teacher, Person student) {
        Optional<Chat> chatOptional = chatRepo.findByTeacherAndStudent(teacher, student);

        return chatOptional.orElseGet(() -> {
            Chat newChat = new Chat(teacher, student);
            return chatRepo.save(newChat);
        });
    }

    public List<Message> findMessages(Integer teacherId, Integer studentId)
    {
        Person teacher = personRepo.findById(teacherId).orElse(null);
        Person student = personRepo.findById(studentId).orElse(null);

        Chat c = chatRepo.findByTeacherAndStudent(teacher, student).orElse(null);

        assert c != null;
        return messageRepo.findByChatChatId(c.getChatId());

    }




}
