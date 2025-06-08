package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.Test1;
import com.example.yourcompany.assessment.entity.TestQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.08 22:01
 **/
@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestions, Integer> {
    List<TestQuestions> getByTestId(Integer testId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TestQuestions t WHERE t.testId = :testId")
    void deleteByTestId(@Param("testId") Integer testId);
}
