package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.TestAnswerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:49
 **/
@Repository
public interface TestAnswerDetailRepository extends JpaRepository<TestAnswerDetail, Integer> {
    List<TestAnswerDetail> findByRecordId(Integer recordId);
}