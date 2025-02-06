package com.example.yourcompany.assessment.dto;

import lombok.Data;


@Data
public class KnowledgeQuestionDTO {
    private Integer questionId;
    private String chapter;
    private String questionType;
    private String content;
    private String []options;
    private String difficulty;
    private String correctAnswer;


}