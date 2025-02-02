package com.example.yourcompany.assessment.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2024.12.13 16:22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResult {
    Integer questionId;
    Boolean isCorrect;
    String correctAnswer;

//    // 以下字段用于算法题目
//    private String submissionId;   // 提交ID
//    private String status;         // 判题状态
//    private String executionTime;  // 执行时间
//    private String memoryUsage;    // 内存使用
//    private String language;       // 编程语言
//    private String code;           // 提交的代码
}
