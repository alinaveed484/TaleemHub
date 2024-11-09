package com.angoor.project.controller;

import com.angoor.project.model.Teacher;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TaleemHub {
	//main controller
	//private final ChatHub chatService;
	private final ResourceHub resourceService;
	//private final Forum forumService;
    private final MentorshipManager managementService;

    @Autowired
    public TaleemHub(MentorshipManager managementService,ResourceHub resourceService) {
        this.managementService = managementService;
        this.resourceService = resourceService;
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
    
    
}
