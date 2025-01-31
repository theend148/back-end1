package com.example.yourcompany.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.yourcompany.assessment.config","com.example.yourcompany.assessment.controller",
        "com.example.yourcompany.assessment.dto",
        "com.example.yourcompany.assessment.entity",
        "com.example.yourcompany.assessment.exception",
        "com.example.yourcompany.assessment.repository",
        "com.example.yourcompany.assessment.security",
        "com.example.yourcompany.assessment.service",
        "com.example.yourcompany.assessment.util",

})
public class AlgorithmAssessmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlgorithmAssessmentApplication.class, args);
    }
} 