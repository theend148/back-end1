package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", statisticsService.getTotalUsers());
        statistics.put("totalQuestions", statisticsService.getTotalQuestions());
        statistics.put("totalSubmissions", statisticsService.getTotalSubmissions());
        statistics.put("chapterStatistics", statisticsService.getChapterStatistics());
        statistics.put("userActivity", statisticsService.getUserActivityStatistics());
        statistics.put("difficultyDistribution", statisticsService.getDifficultyDistribution());
        
        return ResponseEntity.ok(statistics);
    }
} 