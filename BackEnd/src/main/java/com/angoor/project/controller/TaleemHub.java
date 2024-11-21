package com.angoor.project.controller;

import com.angoor.project.dto.TeacherDto;
import com.angoor.project.model.Person;
import com.angoor.project.model.PersonDTO;
import com.angoor.project.model.resource_category;
import com.angoor.project.repository.CommentRepo;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.PostRepo;
import com.angoor.project.service.Forum;
import com.angoor.project.service.MentorshipManager;
import com.angoor.project.service.ResourceHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class TaleemHub {
	//main controller
	//private final ChatHub chatService;
	private final ResourceHub resourceService;
	private final Forum forumService;
    private final MentorshipManager managementService;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final PersonRepo personRepo;

    @Autowired
    public TaleemHub(MentorshipManager managementService,ResourceHub resourceService, PostRepo postRepo, CommentRepo commentRepo, Forum forumService, PersonRepo personRepo) {
        this.managementService = managementService;
        this.resourceService = resourceService;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.forumService = forumService;
        this.personRepo = personRepo;
    }

	//MentorshipManager managementService = new MentorshipManager();



    @GetMapping("/hello")
    @ResponseBody
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, World!");
        return response;
    }

    @GetMapping("/student/select_mentor/display_teachers")
    public String selectMentor_displayTeachers(@RequestParam String subject, Model model) {
        List<TeacherDto> teachers = managementService.displayTeachers(subject);
        model.addAttribute("teachers", teachers); // Add the list of TeacherDTOs to the model
        model.addAttribute("subject", subject);    // Add the subject to the model for display
        return "displayTeachers";  // Name of the Thymeleaf template
    }




    @GetMapping("/student/select_mentor/show_teacher_details")
    @ResponseBody
    public Map<String, Object> selectMentor_showTeacherDetails(@RequestParam Integer teacherID) {
        Map<String, Object> response = new HashMap<>();
        
        response = managementService.showTeacherDetails(teacherID);
        //this map will contain all the details of the single teacher.
        
        return response;
    }
    
    @GetMapping("/student/select_mentor/send_mentor_request")
    @ResponseBody
    public Map<String, Object> selectMentor_sendMentorRequest(@RequestParam Integer teacherID, @RequestParam Integer studentID) {
        Map<String, Object> response = new HashMap<>();
        response = managementService.sendMentorRequest(teacherID,studentID);
        //this map will contain all the details of the single teacher.
        
        return response;
    }
    
    @GetMapping("/resource/share_resource/get_resource_categories")
    @ResponseBody
    public Map<String, Object> shareResource_getResourceCategories(){
    	Map<String, Object> response = new HashMap<>();

    	response = resourceService.getResourceCategories();

    	return response;
    }

    @PostMapping("/resource/share_resource/share_resources")
    @ResponseBody
    public ResponseEntity<String>shareResource_shareResources(
    		 @RequestParam("file") MultipartFile file,           // The file parameter
             @RequestParam("title") String title,                // Other resource details
             @RequestParam("category") resource_category category,
             @RequestParam("uploader_id") Integer uploaderId){


    	return resourceService.uploadResource(file, title, category, uploaderId);

    }

    @GetMapping("/teacher/accept_student/display_students")
    @ResponseBody
    public Map<String, Object> acceptStudents_displayStudents(@RequestParam Integer teacherId) {
        Map<String, Object> response = new HashMap<>();

        response = managementService.displayStudents(teacherId);
        //the map will only contain name and subject of all teachers.

        return response;
    }

    @GetMapping("/teacher/accept_student/show_student_request")
    @ResponseBody
    public Map<String, Object> acceptStudent_showStudentRequest(@RequestParam Integer teacherID,
                                                                @RequestParam Integer studentID) {
        Map<String, Object> response = new HashMap<>();

        response = managementService.showStudentRequest(teacherID, studentID);
        //this map will contain all the details of the single teacher.

        return response;
    }

    @GetMapping("/teacher/accept_student/accept_student")
    @ResponseBody
    public Map<String, Object> acceptStudent_acceptStudent(@RequestParam Integer teacherID,
                                                           @RequestParam Integer studentID) {
        Map<String, Object> response = new HashMap<>();

        response = managementService.acceptStudent(studentID, teacherID);
        //this map will contain all the details of the single teacher.

        return response;
    }



    // Chat Functionalities

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public PersonDTO connect(@Payload PersonDTO personDTO){
        managementService.connect(personDTO);
        return personDTO;
    }

    @MessageMapping ("/user.disconnectUser")
    @SendTo("/user/topic")
    public PersonDTO disconnect(@Payload PersonDTO personDTO) {
        managementService.disconnect(personDTO);
        return personDTO;
    }

    @GetMapping("/users")
    @CrossOrigin(origins = "http://localhost:3000") // Allow requests from frontend origin
    public ResponseEntity<Set<PersonDTO>> displayConnectedUsers(@RequestParam Integer personID, @RequestParam String person_type) {
        Set<PersonDTO> connectedUsers = managementService.getConnectedUsersDTO(personID, person_type);
        return ResponseEntity.ok(connectedUsers);
    }


}
