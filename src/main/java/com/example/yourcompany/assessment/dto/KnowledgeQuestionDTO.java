package com.example.yourcompany.assessment.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Arrays;

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