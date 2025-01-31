package com.example.yourcompany.assessment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Qianyue
 * @Date 2024.12.08 15:25
 **/
// Test.java (Entity)
@Data
@Entity
@Table(name = "tests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Integer testId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "chapter", nullable = false)
    private String chapter;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionCategory questionType;

    @Column(name = "easy_questions_num", nullable = false)
    private Integer easyQuestionsNum;

    @Column(name = "medium_questions_num", nullable = false)
    private Integer mediumQuestionsNum;

    @Column(name = "hard_questions_num", nullable = false)
    private Integer hardQuestionsNum;

    @Column(name = "generate_type", nullable = false)
    private Boolean generateType;
}
