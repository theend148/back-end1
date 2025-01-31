package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.PracticeRecord;
import com.example.yourcompany.assessment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRecordRepository extends JpaRepository<PracticeRecord, Integer> {
    List<PracticeRecord> findByUser(User user);
    List<PracticeRecord> findByUserAndQuestionType(User user, String questionType);
} 