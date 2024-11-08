package com.angoor.project.controller;

import com.angoor.project.service.ChatHub;
import com.angoor.project.service.MentorshipManager;
import com.angoor.project.service.ResourceHub;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaleemHub {
	//main controller

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
