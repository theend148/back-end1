package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {
    private final AdminStatisticsService adminStatisticsService;

    /**
     * 获取用户统计数据
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getUserStatistics());
    }

    /**
     * 获取题目统计数据
     */
    @GetMapping("/questions")
    public ResponseEntity<Map<String, Object>> getQuestionStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getQuestionStatistics());
    }

    /**
     * 获取提交统计数据
     */
    @GetMapping("/submissions")
    public ResponseEntity<Map<String, Object>> getSubmissionStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getSubmissionStatistics());
    }

    /**
     * 获取考试统计数据
     */
    @GetMapping("/tests")
    public ResponseEntity<Map<String, Object>> getTestStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getTestStatistics());
    }

    /**
     * 获取章节详细数据
     */
    @GetMapping("/chapters/detailed")
    public ResponseEntity<Object> getChapterDetailedStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getChapterDetailedStatistics());
    }

    /**
     * 获取用户详细数据
     */
    @GetMapping("/users/detailed")
    public ResponseEntity<Object> getUserDetailedStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getUserDetailedStatistics());
    }

    /**
     * 获取提交详细数据
     */
    @GetMapping("/submissions/detailed")
    public ResponseEntity<Object> getSubmissionDetailedStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getSubmissionDetailedStatistics());
    }

    /**
     * 获取考试详细数据
     */
    @GetMapping("/tests/detailed")
    public ResponseEntity<Object> getTestDetailedStatistics() {
        return ResponseEntity.ok(adminStatisticsService.getTestDetailedStatistics());
    }
}