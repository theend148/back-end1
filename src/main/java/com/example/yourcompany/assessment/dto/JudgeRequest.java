package com.example.yourcompany.assessment.dto;

import lombok.Data;

/**
 * @author Qianyue
 * @Date 2025.01.18 22:10
 **/
@Data
public class JudgeRequest {
    private String sourceCode;
    private Integer languageId;  // judge0的语言ID
    private String stdin;        // 测试用例输入
    private String expectedOutput; // 期望输出
}
