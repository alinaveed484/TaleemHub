package com.angoor.taleemhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "com.angoor")
@EnableJpaRepositories(basePackages = "com.angoor.project.repository")
@EntityScan(basePackages = "com.angoor.project.model")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TaleemHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaleemHubApplication.class, args);
    }
}
