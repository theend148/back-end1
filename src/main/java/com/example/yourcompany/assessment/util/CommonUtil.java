package com.example.yourcompany.assessment.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    
    public long calculateDurationInSeconds(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.SECONDS.between(start, end);
    }
    
    public double calculatePercentage(int correct, int total) {
        if (total == 0) return 0.0;
        return (double) correct / total * 100;
    }
    
    public boolean isValidPassword(String password) {
        // 密码至少8位，包含数字和字母
        return password != null && 
               password.length() >= 8 && 
               password.matches(".*[0-9].*") && 
               password.matches(".*[a-zA-Z].*");
    }
} 