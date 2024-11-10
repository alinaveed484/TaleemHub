package com.angoor.project.controller;

import com.angoor.project.model.Person;
import com.angoor.project.model.PersonDTO;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TaleemHub {
	//main controller
	//private final ChatHub chatService;
	private final ResourceHub resourceService;
	//private final Forum forumService;
    private final MentorshipManager managementService;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public TaleemHub(MentorshipManager managementService,ResourceHub resourceService, PostRepo postRepo, CommentRepo commentRepo) {
        this.managementService = managementService;
        this.resourceService = resourceService;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

	//MentorshipManager managementService = new MentorshipManager();



    @GetMapping("/hello")
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, World!");
        return response;
    }

    @GetMapping("/student/select_mentor/display_teachers")
    public Map<String, Object> selectMentor_displayTeachers(@RequestParam String subject) {
        Map<String, Object> response = new HashMap<>();

        response = managementService.displayTeachers(subject);
        //the map will only contain name and subject of all teachers.

        return response;
    }


    @GetMapping("/student/select_mentor/show_teacher_details")
    public Map<String, Object> selectMentor_showTeacherDetails(@RequestParam Integer teacherID) {
        Map<String, Object> response = new HashMap<>();

        response = managementService.showTeacherDetails(teacherID);
        //this map will contain all the details of the single teacher.

        return response;
    }

    @GetMapping("/student/select_mentor/send_mentor_request")
    public Map<String, Object> selectMentor_sendMentorRequest(@RequestParam Integer teacherID, @RequestParam Integer studentID) {
        Map<String, Object> response = new HashMap<>();
        response = managementService.sendMentorRequest(teacherID,studentID);
        //this map will contain all the details of the single teacher.

        return response;
    }

    // Chat Functionalities

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public Person connect(@Payload Person person){
        managementService.connect(person);
        return person;
    }

    @MessageMapping ("/user.disconnectUser")
    @SendTo("/user/topic")
    public Person disconnect(@Payload Person person) {
        managementService.disconnect(person);
        return person;
    }

    @PostMapping("/users")
    @CrossOrigin(origins = "http://localhost:3000") // Allow requests from frontend origin
    public ResponseEntity<Set<PersonDTO>> displayConnectedUsers(@RequestBody Person person) {
        Set<PersonDTO> connectedUsers = managementService.getConnectedUsersDTO(person);
        return ResponseEntity.ok(connectedUsers);
    }


}
