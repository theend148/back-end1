package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.entity.QuestionCategory;
import lombok.Data;

@Data
public class PracticeSubmitDTO {
    private Integer userId;
    private Integer questionId;
    private String answer;
    private Boolean isCorrect;

    private QuestionCategory questionType;

} 