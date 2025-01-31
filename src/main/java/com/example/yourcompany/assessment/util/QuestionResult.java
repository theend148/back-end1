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
}
