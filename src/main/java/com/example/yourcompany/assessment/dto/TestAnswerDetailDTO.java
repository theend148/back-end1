package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:40
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerDetailDTO {
    private Integer detailId;
    private Integer recordId;

    private Integer questionId;
    private String userAnswer;
    private Boolean isCorrect;

    public TestAnswerDetailDTO(Integer recordId, Integer questionId, String userAnswer, Boolean isCorrect) {
        this.recordId = recordId;
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }
}
