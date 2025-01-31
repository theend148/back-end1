package com.example.yourcompany.assessment.entity;

//import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

//import javax.persistence.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "knowledge_questions")
public class KnowledgeQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(nullable = false)
    private String chapter;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "JSON")
    private String options;

    @Column(name = "correct_answer", nullable = false, columnDefinition = "TEXT")
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.easy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
} 