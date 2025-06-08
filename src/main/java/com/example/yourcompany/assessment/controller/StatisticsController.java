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

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(Map.of(
                "totalUsers", statisticsService.getTotalUsers(),
                "totalQuestions", statisticsService.getTotalQuestions(),
                "totalSubmissions", statisticsService.getTotalSubmissions()));
    }

    @GetMapping("/user-activity")
    public ResponseEntity<Map<String, Object>> getUserActivity() {
        return ResponseEntity.ok(statisticsService.getUserActivityStatistics());
    }

    @GetMapping("/chapter-distribution")
    public ResponseEntity<Map<String, Object>> getChapterDistribution() {
        return ResponseEntity.ok(statisticsService.getChapterStatistics());
    }

    @GetMapping("/difficulty-distribution")
    public ResponseEntity<Map<String, Object>> getDifficultyDistribution() {
        return ResponseEntity.ok(statisticsService.getDifficultyDistribution());
    }

    @GetMapping("/submission-trend")
    public ResponseEntity<Map<String, Object>> getSubmissionTrend() {
        return ResponseEntity.ok(statisticsService.getSubmissionTrend());
    }
}