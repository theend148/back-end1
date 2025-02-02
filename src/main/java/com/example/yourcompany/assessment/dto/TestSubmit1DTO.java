package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Qianyue
 * @Date 2025.02.02 17:33
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSubmit1DTO {
    private Integer testId;
    private Integer userId;
    private LocalDateTime startTime;
    private Integer totalTime;
    // 对于算法题目，answers存储的是 questionId -> submissionId 的映射
    private Map<Integer, String> answers;  // key: questionId, value: submissionId
}
