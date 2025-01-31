package com.example.yourcompany.assessment.dto;

import com.example.yourcompany.assessment.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.08 17:34
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "考试时长不能为空")
    private Integer duration;

    @NotBlank(message = "章节不能为空")
    private String chapter;

    @NotNull(message = "题目类型不能为空")
    private QuestionCategory questionType;

    @NotNull(message = "是否自动生成不能为空")
    private Boolean isAutoGenerate;

    // 自动生成模式需要的字段
    private Integer easyQuestionsNum;
    private Integer mediumQuestionsNum;
    private Integer hardQuestionsNum;


    // 手动选择模式需要的字段
    private List<Integer> questionIds;
}

