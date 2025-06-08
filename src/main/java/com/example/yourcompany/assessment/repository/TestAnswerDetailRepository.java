package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.TestAnswerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:49
 **/
@Repository
public interface TestAnswerDetailRepository extends JpaRepository<TestAnswerDetail, Integer> {
    List<TestAnswerDetail> findByRecordId(Integer recordId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TestAnswerDetail t WHERE t.questionId = :questionId")
    void deleteByQuestionId(@Param("questionId") Integer questionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TestAnswerDetail t WHERE t.recordId = :recordId")
    void deleteByRecordId(@Param("recordId") Integer recordId);
}