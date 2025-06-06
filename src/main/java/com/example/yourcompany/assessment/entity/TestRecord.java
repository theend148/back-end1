package com.example.yourcompany.assessment.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:27
 **/
@Data
@Entity
@Table(name = "test_record")
public class TestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    public TestRecord() {
    }

    @Column(name = "test_id")
    private Integer testId;

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "start_time")

    private LocalDateTime startTime;
    @Column(name = "total_time")

    private Integer totalTime;

   
    @Column(name = "total_questions")

    private Integer totalQuestions;
    @Column(name = "correct_count")

    private Integer correctCount;
    @Column(name = "easy_total")

    private Integer easyTotal;
    @Column(name = "easy_correct")

    private Integer easyCorrect;
    @Column(name = "medium_total")

    private Integer mediumTotal;
    @Column(name = "medium_correct")

    private Integer mediumCorrect;
    @Column(name = "hard_total")

    private Integer hardTotal;
    @Column(name = "hard_correct")

    private Integer hardCorrect;

    private String status;

    public TestRecord(Integer recordId, Integer testId, Integer userId, LocalDateTime startTime, Integer totalTime, Integer totalQuestions, Integer correctCount, Integer easyTotal, Integer easyCorrect, Integer mediumTotal, Integer mediumCorrect, Integer hardTotal, Integer hardCorrect, String status) {
        this.recordId = recordId;
        this.testId = testId;
        this.userId = userId;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.easyTotal = easyTotal;
        this.easyCorrect = easyCorrect;
        this.mediumTotal = mediumTotal;
        this.mediumCorrect = mediumCorrect;
        this.hardTotal = hardTotal;
        this.hardCorrect = hardCorrect;
        this.status = status;
    }

    public TestRecord(Integer testId, Integer userId, LocalDateTime startTime, Integer totalTime, Integer totalQuestions, Integer correctCount, Integer easyTotal, Integer easyCorrect, Integer mediumTotal, Integer mediumCorrect, Integer hardTotal, Integer hardCorrect, String status) {
        this.testId = testId;
        this.userId = userId;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.easyTotal = easyTotal;
        this.easyCorrect = easyCorrect;
        this.mediumTotal = mediumTotal;
        this.mediumCorrect = mediumCorrect;
        this.hardTotal = hardTotal;
        this.hardCorrect = hardCorrect;
        this.status = status;
    }

}
