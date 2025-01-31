package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2024.12.08 22:00
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionsDTO {
    private Integer id;

    private Integer testId;
    private Integer questionId;
    private QuestionCategory questionType;  // 枚举类型


}
