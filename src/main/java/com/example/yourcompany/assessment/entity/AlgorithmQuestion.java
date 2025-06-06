package com.example.yourcompany.assessment.entity;

//import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "algorithm_questions")
public class AlgorithmQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(nullable = false)
    private String chapter;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.easy;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_scope", nullable = false)
    private QuestionScope questionScope = QuestionScope.practice;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "input_format", columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "sample_input", columnDefinition = "TEXT")
    private String sampleInput;

    @Column(name = "sample_output", columnDefinition = "TEXT")
    private String sampleOutput;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}