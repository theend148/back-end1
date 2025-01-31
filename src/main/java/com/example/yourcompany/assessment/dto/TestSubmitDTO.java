package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Qianyue
 * @Date 2024.12.10 17:04
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSubmitDTO {
    private Integer testId;
    private Integer userId;
    private LocalDateTime startTime;
    private Integer totalTime;

    // 使用Map存储题目ID和答案的对应关系
    private Map<Integer, String> answers;  // key: questionId, value: userAnswer
}