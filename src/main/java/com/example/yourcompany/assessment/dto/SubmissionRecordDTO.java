package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:17
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRecordDTO {

    private Integer id;
    private LocalDateTime submissionTime;
    private String executionTime;
    private String memoryConsumption;
    private String status;
    private String language;
    private Integer questionId;
    private Integer userId;
    private String sourceCode;
}

