package com.example.yourcompany.assessment.entity;

//import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

//import javax.persistence.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "code_submissions")
public class CodeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Integer submissionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private AlgorithmQuestion question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    private SubmissionResult result;

    @CreationTimestamp
    @Column(name = "submission_time")
    private LocalDateTime submissionTime;
} 