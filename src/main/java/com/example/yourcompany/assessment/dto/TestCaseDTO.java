package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2025.01.18 21:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDTO {
    private Integer caseId;
    private String inputData;
    private String outputData;
    private boolean isPublic;
}
