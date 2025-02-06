package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.CodeSubmitRequestDTO;
import com.example.yourcompany.assessment.dto.SubmissionRecordDTO;
import com.example.yourcompany.assessment.entity.CodeSubmission;
import com.example.yourcompany.assessment.service.SubmissionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class CodeSubmissionController {
    private final SubmissionRecordService submissionRecordService;

    @PostMapping("/batch")
    public ResponseEntity<List<SubmissionRecordDTO>> submitCodeBatch(
            @Valid @RequestBody List<CodeSubmitRequestDTO> codeSubmitRequests) {
        return ResponseEntity.ok(submissionRecordService.createBatchSubmissionRecord(codeSubmitRequests));
    }

    @PostMapping
    public ResponseEntity<SubmissionRecordDTO> submitCode(
            @Valid @RequestBody CodeSubmitRequestDTO codeSubmitRequestDTO) {
        return ResponseEntity.ok(submissionRecordService.createSubmissionRecord(codeSubmitRequestDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@PathVariable Integer userId) {
//        com.example.yourcompany.assessment.entity.CodeSubmission.
        return null;
//        return ResponseEntity.ok(codeSubmissionService.getUserSubmissions(userId));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<SubmissionRecordDTO>> getSubmissionsByQuestionId(@PathVariable Integer questionId) {
        List<SubmissionRecordDTO> submissions = submissionRecordService.getSubmissionRecordsByQuestionId(questionId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/question/{userId}/{questionId}")
    public ResponseEntity<List<SubmissionRecordDTO>> getSubmissionsByQuestionIdAndUserId(@PathVariable Integer userId,@PathVariable Integer questionId) {
        List<SubmissionRecordDTO> submissions = submissionRecordService.getSubmissionRecordsByQuestionIdAndUserId(userId,questionId);
        return ResponseEntity.ok(submissions);
    }



    @GetMapping("/user/{userId}/question/{questionId}")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissionsByQuestion(
            @PathVariable Integer userId,
            @PathVariable Integer questionId) {
        return null;
//        return ResponseEntity.ok(codeSubmissionService.getUserSubmissionsByQuestion(userId, questionId));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<CodeSubmission> getSubmission(@PathVariable Integer submissionId) {
//        return ResponseEntity.ok(codeSubmissionService.getSubmissionById(submissionId));
        return null;
    }

    @GetMapping("/user/{userId}/rate")
    public ResponseEntity<Double> getUserSubmissionRate(@PathVariable Integer userId) {
        return null;
//        return ResponseEntity.ok(codeSubmissionService.calculateUserSubmissionRate(userId));
    }
} 