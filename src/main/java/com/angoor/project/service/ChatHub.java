package com.angoor.project.service;

import com.angoor.project.model.Chat;
import com.angoor.project.model.Message;
import com.angoor.project.model.Person;
import com.angoor.project.repository.ChatRepo;
import com.angoor.project.repository.MessageRepo;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.CalenderRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ChatHub {

    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;

    @Autowired
    public ChatHub(ChatRepo chatRepo, MessageRepo messageRepo) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
    }

    public Chat saveMessage(Message message) {
        // Retrieve or create the Chat
        Chat chat = getOrCreateChat(message.getChat().getTeacher(), message.getChat().getStudent());

        // Set the Chat in the Message entity
        message.setChat(chat);

        // Save the message in the repository
        messageRepo.save(message);

        return chat;
    }

    private Chat getOrCreateChat(Person teacher, Person student) {
        Optional<Chat> chatOptional = chatRepo.findByTeacherAndStudent(teacher, student);

        return chatOptional.orElseGet(() -> {
            Chat newChat = new Chat(teacher, student);
            return chatRepo.save(newChat);
        });
    }



}
