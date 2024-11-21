package com.angoor.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configure security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorized -> {
            authorized.requestMatchers("/css/**", "/js/**", "/index.html", "/scripts/**").permitAll();
            authorized.anyRequest().authenticated();
        });
        return httpSecurity.build();
    }
}*/
