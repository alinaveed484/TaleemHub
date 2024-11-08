package com.angoor.project.controller;

import com.angoor.project.service.ChatHub;
import com.angoor.project.service.MentorshipManager;
import com.angoor.project.service.ResourceHub;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TaleemHub {
	//main controller

    @GetMapping("/hello")
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, World!");
        return response;
    }
}
