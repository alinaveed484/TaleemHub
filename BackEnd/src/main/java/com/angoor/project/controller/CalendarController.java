package com.angoor.project.controller;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.angoor.project.service.CalendarService;





@RestController
public class CalendarController {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    
    private Integer personID;
    
    private final CalendarService calendarService;
    
    @Autowired
    public CalendarController(CalendarService calendarService) {
    	this.calendarService = calendarService;
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
    
}