package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.CodeSubmitRequestDTO;
import com.example.yourcompany.assessment.dto.SubmissionRecordDTO;
import com.example.yourcompany.assessment.entity.TestCaseResult;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:18
 **/
public interface SubmissionRecordService {

    SubmissionRecordDTO createSubmissionRecord(CodeSubmitRequestDTO submissionRecordDTO);

    List<SubmissionRecordDTO> createBatchSubmissionRecord(List<CodeSubmitRequestDTO> submissionRecordDTOs);

    SubmissionRecordDTO getSubmissionRecordById(Integer id);

    List<SubmissionRecordDTO> getAllSubmissionRecords();

    List<SubmissionRecordDTO> getSubmissionRecordsByUserId(Integer userId);

    List<SubmissionRecordDTO> getSubmissionRecordsByQuestionId(Integer questionId);

    List<SubmissionRecordDTO> getSubmissionRecordsByQuestionIdAndUserId(Integer userId, Integer questionId);

    List<TestCaseResult> getTestCaseResults(Integer submissionId);
}
