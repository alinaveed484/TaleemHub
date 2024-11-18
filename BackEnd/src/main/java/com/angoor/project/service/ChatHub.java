package com.angoor.project.service;

import com.angoor.project.model.*;
import com.angoor.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ChatHub {

    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ChatHub(ChatRepo chatRepo, MessageRepo messageRepo, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }



    public Message saveMessage(MessageDTO messageDTO) {
        // Retrieve or create the Chat
        String type_sender = teacherRepository.findPersonTypeById(messageDTO.getSenderId());

        Integer teacherID;
        Integer studentID;

        Teacher t;
        Student s;

        // Convert MessageDTO to Message entity
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setType(MessageType.valueOf(messageDTO.getType().toUpperCase()));
        message.setTimeStamp(messageDTO.getTimeStamp());

        if (type_sender.equals("TEACHER"))
        {
            teacherID = messageDTO.getSenderId();
            studentID = messageDTO.getRecipientId();

            t = teacherRepository.findById(teacherID).orElse(null);
            s = studentRepository.findById(studentID).orElse(null);

            message.setSender(t);
            message.setRecipient(s);
        }
        else
        {
            studentID = messageDTO.getSenderId();
            teacherID = messageDTO.getRecipientId();

            t = teacherRepository.findById(teacherID).orElse(null);
            s = studentRepository.findById(studentID).orElse(null);

            message.setSender(s);
            message.setRecipient(t);
        }

        Chat chat = getOrCreateChat(teacherID, studentID);

        // Set the Chat in the Message entity
        message.setChat(chat);

        // Save the message in the repository
        messageRepo.save(message);

        return message;
    }

    private Chat getOrCreateChat(Integer teacherID, Integer studentID) {
        Optional<Chat> chatOptional = chatRepo.findByTeacher_idAndStudent_id(teacherID, studentID);
        Teacher t = teacherRepository.getReferenceById(teacherID);
        Student s = studentRepository.getReferenceById(studentID);
        return chatOptional.orElseGet(() -> {
            Chat newChat = new Chat(t, s);
            return chatRepo.save(newChat);
        });
    }

    public List<Message> findMessages(Integer id1, Integer id2)
    {
        // here i have to make sure the teacher_id is an actual id for the teacher
        String type = teacherRepository.findPersonTypeById(id1);

        Chat c;
        if (Objects.equals(type, "TEACHER"))
        {
            // id1 is teacher
            c = chatRepo.findByTeacher_idAndStudent_id(id1, id2).orElse(null);
        }
        else
        {
            c = chatRepo.findByTeacher_idAndStudent_id(id2, id1).orElse(null);
        }

        if (c == null)
        {
            return Collections.emptyList(); // Returns an immutable empty list of type List<Message>
        }
        return messageRepo.findByChatChatId(c.getChatId());

    }




}
