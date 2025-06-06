package com.example.yourcompany.assessment.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2025.01.18 21:06
 **/

@Data
public class AddAlgorithmQuestionRequestDTO {
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
    private List<TestCase> testCases;

    @Data
    public static class TestCase {
        private String input;
        private String output;
        private boolean isPublic;
    }
}
