package com.angoor.taleemhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.angoor")
@EnableJpaRepositories(basePackages = "com.angoor.project.repository")
@EntityScan(basePackages = "com.angoor.project.model")
public class TaleemHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaleemHubApplication.class, args);
    }
}
