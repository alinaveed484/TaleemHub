package com.angoor.project.controller;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.angoor.project.dto.StudentDto;
import com.angoor.project.model.Person;
import com.angoor.project.model.Teacher;
import com.angoor.project.service.CalendarService;
import com.angoor.project.service.MentorshipManager;





@Controller
public class CalendarController {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    
    private Integer personID;
    
    private final CalendarService calendarService;
   
    private final MentorshipManager mentorshipService;
    
    @Autowired
    public CalendarController(CalendarService calendarService, MentorshipManager mentorshipService) {
    	this.calendarService = calendarService;
		this.mentorshipService = mentorshipService;
    }
    
    @GetMapping("/auth/googleCalender")
    public ResponseEntity<Void> redirectToGoogle(@RequestParam Integer personID) {
    	this.personID = personID;
        String authorizationUri = "https://accounts.google.com/o/oauth2/auth?" +
                                  "client_id=" + clientId +
                                  "&redirect_uri=" + redirectUri +
                                  "&response_type=code" +
                                  "&scope=https://www.googleapis.com/auth/calendar.events" +
                                  "&access_type=offline";
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authorizationUri)).build();
    }
    
    @GetMapping("/auth/callback")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) {
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or missing code");
        }

        // Exchange the code for tokens
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", code);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, Map.class);

        Map<String, Object> tokens = response.getBody();
        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");
        Integer expiresIn = (Integer) tokens.get("expires_in");  // expires_in is typically an Integer

        // If you want to calculate the expiration time as LocalDateTime
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(expiresIn);

        // Store tokens securely for the user
        // ... (e.g., save to database)
        boolean worked = calendarService.saveTokens(personID,accessToken,refreshToken,expirationTime);
        if(worked)
        	return ResponseEntity.ok("Done!");
        else
        	return ResponseEntity.ok("There was some problem!");
    }
    
    @PostMapping("/schedule/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getScheduleData(@RequestBody Map<String, String> payload) {
        String uid = payload.get("uid");
        Teacher teacher = calendarService.getTeacherByUid(uid);
        Set <StudentDto> students = mentorshipService.getStudentDTOs(teacher);
        Map<String, Object> response = new HashMap<>();
        response.put("students", students);
        response.put("teacherId", teacher.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/schedule/createEvent")
    @ResponseBody
    public ResponseEntity<String> createSchedule(
            @RequestParam Integer teacherId,
            @RequestParam Integer studentId,
            @RequestParam String startTime,
            @RequestParam String endTime) {

        // Example: Call the calendarService to create the schedule
        boolean result = calendarService.createSchedule(teacherId, studentId, startTime, endTime);

        if (result) {
            return ResponseEntity.ok("Event created successfully!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating event.");
    }
    
    
    @GetMapping("/schedule")
    public String getScheduleDataHTML() {
    	return "schedule-event";
    }
    
    
}
