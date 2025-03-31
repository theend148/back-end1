package com.example.yourcompany.assessment.service;

import java.util.Map;

public interface StatisticsService {
    // 基础统计
    int getTotalUsers();
    int getTotalQuestions();
    int getTotalSubmissions();
    
    // 用户活跃度统计
    Map<String, Object> getUserActivityStatistics();
    
    // 章节统计
    Map<String, Object> getChapterStatistics();
    
    // 难度分布统计
    Map<String, Object> getDifficultyDistribution();
    
    // 提交趋势统计
    Map<String, Object> getSubmissionTrend();
}