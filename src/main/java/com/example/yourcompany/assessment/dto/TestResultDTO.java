package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.util.QuestionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Qianyue
 * @Date 2024.12.13 16:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDTO {

    private Integer testId;
    private Integer userId;

    // Overall correctness rate
    private double overallAccuracy;

    // Accuracy rates by difficulty level
    private Map<String, Double> accuracyByDifficulty; // e.g., "easy" -> 0.8, "medium" -> 0.7

    // List of question details
    private List<QuestionResult> questionResults;
}
