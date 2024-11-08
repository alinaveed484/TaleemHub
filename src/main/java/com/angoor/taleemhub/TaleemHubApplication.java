package com.angoor.taleemhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.angoor")
public class TaleemHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaleemHubApplication.class, args);
    }
}
