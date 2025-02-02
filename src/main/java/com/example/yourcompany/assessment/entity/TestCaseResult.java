package com.example.yourcompany.assessment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2025.02.02 17:36
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private Boolean passed;
}
