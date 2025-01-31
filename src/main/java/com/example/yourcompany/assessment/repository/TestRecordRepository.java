package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Qianyue
 * @Date 2024.12.10 16:46
 **/
@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, Integer> {
    List<TestRecord> findByUserId(Integer userId);
    Optional<TestRecord> findByTestIdAndUserId(Integer testId, Integer userId);
    List<TestRecord> getByTestId(Integer testId);
}
