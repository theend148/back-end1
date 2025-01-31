package com.example.yourcompany.assessment.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:28
 **/
@Data
@Entity
@Table(name = "test_answer_detail")
public class TestAnswerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "question_id")
    private Integer questionId;
    @Column(name = "user_answer")
    private String userAnswer;
    @Column(name = "is_correct")
    private Boolean isCorrect;

    public TestAnswerDetail(Integer recordId, Integer questionId, String userAnswer, Boolean isCorrect) {
        this.recordId = recordId;
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }



    public TestAnswerDetail() {
    }


}
