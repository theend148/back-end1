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
 * @Date 2025.01.17 20:57
 **/
@Data
@Entity
@Table(name = "submission_records")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @CreationTimestamp
    @Column(name = "submission_time")
    private LocalDateTime submissionTime;

    @Column(name = "execution_time", nullable = false, length = 10)
    private String executionTime;

    @Column(name = "memory_consumption", nullable = false, length = 10)
    private String memoryConsumption;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AlgorithmQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    @Basic(fetch = FetchType.EAGER)
    private String sourceCode;
}

