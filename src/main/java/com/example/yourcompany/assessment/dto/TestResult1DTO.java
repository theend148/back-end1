package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.util.QuestionResult1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Qianyue
 * @Date 2025.02.02 17:33
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResult1DTO {
    private Integer testId;
    private Integer userId;
    private double overallAccuracy;
    private Map<String, Double> accuracyByDifficulty;
    private List<QuestionResult1> questionResults;
}