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
 * @Date 2024.12.08 21:58
 **/
@Data
@Entity
@Table(name = "test_questions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "test_id")
    private Integer testId;


    @Column(name = "question_id")
    private Integer questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionCategory questionType;
}
