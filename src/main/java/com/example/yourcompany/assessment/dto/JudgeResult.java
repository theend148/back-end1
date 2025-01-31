package com.example.yourcompany.assessment.dto;

import lombok.Data;

/**
 * @author Qianyue
 * @Date 2025.01.18 22:10
 **/
@Data
public class JudgeResult {
    private String status;     // 状态：AC, WA, TLE, MLE等
    private String stdout;     // 程序输出
    private String stderr;     // 错误信息
    private Double time;       // 执行时间
    private Double memory;     // 内存使用
}
