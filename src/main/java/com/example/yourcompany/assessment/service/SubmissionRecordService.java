package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.CodeSubmitRequestDTO;
import com.example.yourcompany.assessment.dto.SubmissionRecordDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:18
 **/
public interface SubmissionRecordService {

    SubmissionRecordDTO createSubmissionRecord(CodeSubmitRequestDTO submissionRecordDTO);

    SubmissionRecordDTO getSubmissionRecordById(Integer id);

    List<SubmissionRecordDTO> getAllSubmissionRecords();

    List<SubmissionRecordDTO> getSubmissionRecordsByUserId(Integer userId);

    List<SubmissionRecordDTO> getSubmissionRecordsByQuestionId(Integer questionId);
}

