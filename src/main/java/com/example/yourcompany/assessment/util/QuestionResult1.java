package com.example.yourcompany.assessment.util;

import com.example.yourcompany.assessment.entity.TestCase;
import com.example.yourcompany.assessment.entity.TestCaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2025.02.02 17:34
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResult1 {
    private Integer questionId;
    private Boolean isCorrect;
    private String submissionId;
    private String status;         // 判题状态 (例如: "100.00%", "Wrong Answer", "Time Limit Exceeded")
    private String executionTime;  // 执行时间
    private String memoryUsage;    // 内存使用
    private String language;       // 编程语言
    private String code;           // 提交的代码
    private String errorMessage;   // 错误信息（如果有）
    private List<TestCaseResult> testCases; // 测试用例结果
}
