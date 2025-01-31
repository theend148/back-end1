package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.entity.QuestionCategory;
import com.example.yourcompany.assessment.entity.Test1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.08 17:34
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTestResponse {
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

    List<Integer> questionIds;
    private Boolean isAutoGenerate;



    public GetTestResponse(Test1 test11, List<Integer> questionsIds,Boolean isAutoGenerate) {
        this.testId = test11.getTestId();
        this.title = test11.getTitle();
        this.description = test11.getDescription();
        this.duration = test11.getDuration();
        this.chapter = test11.getChapter();
        this.totalQuestions = test11.getTotalQuestions();
        this.createdAt = test11.getCreatedAt();
        this.questionType = test11.getQuestionType();
        this.easyQuestionsNum = test11.getEasyQuestionsNum();
        this.mediumQuestionsNum = test11.getMediumQuestionsNum();
        this.hardQuestionsNum = test11.getHardQuestionsNum();
        this.questionIds = questionsIds  ;
        this.isAutoGenerate=isAutoGenerate;
    }
}

