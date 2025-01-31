package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.Test1;
import com.example.yourcompany.assessment.entity.TestQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.08 22:01
 **/
public interface TestQuestionRepository extends JpaRepository<TestQuestions, Integer> {
    List<TestQuestions> getByTestId(Integer testId);
}
