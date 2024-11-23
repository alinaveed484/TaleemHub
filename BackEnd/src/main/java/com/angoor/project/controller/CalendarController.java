package com.angoor.project.controller;


import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    
    
    private final CalendarService calendarService;
   
    private final MentorshipManager mentorshipService;
    
    @Autowired
    public CalendarController(CalendarService calendarService, MentorshipManager mentorshipService) {
    	this.calendarService = calendarService;
		this.mentorshipService = mentorshipService;
    }
    
    @GetMapping("/auth/button")
    public String authingFunction(){
    	return "calendar-button";
    }
    
    @GetMapping("/auth/checkCalendarStatus")
    public ResponseEntity<Map<String, Boolean>> checkCalendarStatus(@RequestParam String firebaseUid) {
        Person person = mentorshipService.findByFirebaseUid(firebaseUid);
        Map<String, Boolean> response = new HashMap<>();

        if (person == null || person.getCalendartoken() == null) {
            response.put("showButton", true);
        } else {
            boolean isRefreshTokenValid = calendarService.checkRefreshToken(person.getCalendartoken().getRefreshToken());
            response.put("showButton", !isRefreshTokenValid);
        }

        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/auth/googleCalendar")
    public ResponseEntity<Void> redirectToGoogle(@RequestParam String firebaseUid) {
        String state = URLEncoder.encode(firebaseUid, StandardCharsets.UTF_8);
        String authorizationUri = "https://accounts.google.com/o/oauth2/auth?" +
                                  "client_id=" + clientId +
                                  "&redirect_uri=" + redirectUri +
                                  "&response_type=code" +
                                  "&scope=https://www.googleapis.com/auth/calendar.events" +
                                  "&access_type=offline" +
                                  "&state=" + state;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authorizationUri)).build();
    }
    
    @GetMapping("/auth/callback")
    public ResponseEntity<String> handleGoogleCallback(
            @RequestParam("code") String code, 
            @RequestParam("state") String state) {

        if (code == null || code.isEmpty() || state == null || state.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid or missing code or state");
        }

        String firebaseUid = URLDecoder.decode(state, StandardCharsets.UTF_8);

        try {
            // Token exchange logic
	        // Proceed with token exchange as before
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
	        Integer expiresIn = (Integer) tokens.get("expires_in");
	
	        // Calculate expiration time
	        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(expiresIn);
	
	        // Save tokens using the uid from state
	        boolean worked = calendarService.saveTokens(firebaseUid, accessToken, refreshToken, expirationTime);

	        if (worked) {
	            // JavaScript response for alert and redirection
	            String htmlResponse = "<!DOCTYPE html>" +
	                    "<html>" +
	                    "<head><title>Authentication</title></head>" +
	                    "<body>" +
	                    "<script>" +
	                    "alert('Done authentication!');" +
	                    "window.location.href = '/';" +
	                    "</script>" +
	                    "</body>" +
	                    "</html>";
	            return ResponseEntity.ok(htmlResponse);
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("<p>There was some problem!</p>");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("<p>Error during authentication: " + e.getMessage() + "</p>");
	    }
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
