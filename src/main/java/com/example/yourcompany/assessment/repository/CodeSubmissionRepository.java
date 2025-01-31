package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.CodeSubmission;
import com.example.yourcompany.assessment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Integer> {
    List<CodeSubmission> findByUser(User user);
    List<CodeSubmission> findByUserOrderBySubmissionTimeDesc(User user);
} 