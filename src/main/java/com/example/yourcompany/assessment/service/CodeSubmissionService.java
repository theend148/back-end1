package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.entity.CodeSubmission;
import java.util.List;

public interface CodeSubmissionService {
    CodeSubmission submitCode(Integer userId, Integer questionId, String code);
    List<CodeSubmission> getUserSubmissions(Integer userId);
    List<CodeSubmission> getUserSubmissionsByQuestion(Integer userId, Integer questionId);
    CodeSubmission getSubmissionById(Integer submissionId);
    double calculateUserSubmissionRate(Integer userId);
} 