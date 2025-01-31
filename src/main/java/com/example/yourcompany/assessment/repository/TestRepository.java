package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.Test1;
import com.example.yourcompany.assessment.entity.TestQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.08 15:29
 **/
public interface TestRepository extends JpaRepository<Test1, Integer> {
    Test1 getByTestId(Integer id);
}
