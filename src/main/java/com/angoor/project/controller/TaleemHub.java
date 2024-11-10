package com.angoor.project.controller;

import com.angoor.project.model.Teacher;
import com.angoor.project.model.resource_category;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import com.angoor.project.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
    
    @GetMapping("/resource/upload_resources/get_resource_categories")
    public Map<String, Object> upload_resources_getResourceCategories(){
    	Map<String, Object> response = new HashMap<>();
    	response = resourceService.getResourceCategories();
    	return response;
    }
    
    @PostMapping("/resource/upload_resources/upload_resource")
    public ResponseEntity<String> upload_resources_uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("category") resource_category category,  // Enum parameter
            @RequestParam("uploader_id") Integer uploaderId) {
    	
    	return resourceService.uploadResource(file,title,category,uploaderId);
    }
    
}
