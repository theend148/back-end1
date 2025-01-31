package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Qianyue
 * @Date 2024.12.08 15:23
 **/
// TestDTO.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test1DTO {
    private Integer testId;
    private String title;
    private String description;
    private Integer duration;  // 考试时长（秒）
    private String chapter;
    private Integer totalQuestions;
    private LocalDateTime createdAt;
    private QuestionCategory questionType;  // 枚举类型
    private Integer easyQuestionsNum;
    private Integer mediumQuestionsNum;
    private Integer hardQuestionsNum;

    private Boolean generateType;

}
