package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.SubmissionRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:19
 **/
public interface SubmissionRecordRepository extends JpaRepository<SubmissionRecord, Integer> {

//    List<SubmissionRecord> findByUserId(Integer userId);
    @Query("SELECT s FROM SubmissionRecord s WHERE s.question.questionId = :questionId ORDER BY s.submissionTime DESC")
    List<SubmissionRecord> findByQuestionQuestionId(@Param("questionId") Integer questionId);
//    @EntityGraph(attributePaths = {"sourceCode"})
//    List<SubmissionRecord> findByQuestionQuestionId(Integer questionId);
//    List<SubmissionRecord> findByQuestionQuestionId(Integer questionId);

//    SubmissionRecord findById(Integer id);
}

