package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.repository.AlgorithmQuestionRepository;
import com.example.yourcompany.assessment.repository.CodeSubmissionRepository;
import com.example.yourcompany.assessment.repository.UserRepository;
import com.example.yourcompany.assessment.service.CodeSubmissionService;
import com.example.yourcompany.assessment.entity.AlgorithmQuestion;
import com.example.yourcompany.assessment.entity.CodeSubmission;
import com.example.yourcompany.assessment.entity.SubmissionResult;
import com.example.yourcompany.assessment.entity.User;
//import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeSubmissionServiceImpl implements CodeSubmissionService {
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final UserRepository userRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;

    @Override
    public CodeSubmission submitCode(Integer userId, Integer questionId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        AlgorithmQuestion question = algorithmQuestionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        CodeSubmission submission = new CodeSubmission();
        submission.setUser(user);
        submission.setQuestion(question);
        submission.setCode(code);
        
        // 这里需要添加代码评判逻辑
        submission.setResult(SubmissionResult.pending);
        
        return codeSubmissionRepository.save(submission);
    }

    @Override
    public List<CodeSubmission> getUserSubmissions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return codeSubmissionRepository.findByUser(user);
    }

    @Override
    public List<CodeSubmission> getUserSubmissionsByQuestion(Integer userId, Integer questionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        AlgorithmQuestion question = algorithmQuestionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        
        return codeSubmissionRepository.findByUserOrderBySubmissionTimeDesc(user);
    }

    @Override
    public CodeSubmission getSubmissionById(Integer submissionId) {
        return codeSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
    }

    @Override
    public double calculateUserSubmissionRate(Integer userId) {
        List<CodeSubmission> submissions = getUserSubmissions(userId);
        if (submissions.isEmpty()) {
            return 0.0;
        }

        long correctCount = submissions.stream()
                .filter(s -> s.getResult() == SubmissionResult.correct)
                .count();

        return (double) correctCount / submissions.size();
    }
} 