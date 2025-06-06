package com.example.yourcompany.assessment.dto;

import lombok.Data;

@Data
public class AlgorithmQuestionDTO {
    private Integer questionId;
    private String chapter;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String sampleInput;
    private String sampleOutput;

    private String difficulty;
    private String questionScope;

    private String constraints;
}