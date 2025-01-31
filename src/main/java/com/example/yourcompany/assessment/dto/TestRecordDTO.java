package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordDTO {
    private Integer recordId;
    private Integer testId;
    private Integer userId;
    private LocalDateTime startTime;
    private Integer totalTime;

    private Integer totalQuestions;
    private Integer correctCount;

    private Integer easyTotal;
    private Integer easyCorrect;

    private Integer mediumTotal;
    private Integer mediumCorrect;

    private Integer hardTotal;
    private Integer hardCorrect;

    private String status;

}
