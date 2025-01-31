package com.example.yourcompany.assessment.entity;

/**
 * @author Qianyue
 * @Date 2025.01.18 21:28
 **/
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_cases")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Integer caseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AlgorithmQuestion question;

    @Column(name = "input_data", nullable = false, columnDefinition = "TEXT")
    private String inputData;

    @Column(name = "output_data", nullable = false, columnDefinition = "TEXT")
    private String outputData;

    @Column(name = "is_public")
    private boolean isPublic;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 可选：添加一些业务方法
    public boolean matchesOutput(String actualOutput) {
        return this.outputData.trim().equals(actualOutput.trim());
    }
}
