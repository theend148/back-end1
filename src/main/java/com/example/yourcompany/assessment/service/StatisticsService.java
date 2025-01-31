package com.example.yourcompany.assessment.service;

import java.util.Map;

public interface StatisticsService {
    int getTotalUsers();
    int getTotalQuestions();
    int getTotalSubmissions();
    Map<String, Double> getChapterStatistics();
    Map<String, Integer> getUserActivityStatistics();
    Map<String, Double> getDifficultyDistribution();
} 