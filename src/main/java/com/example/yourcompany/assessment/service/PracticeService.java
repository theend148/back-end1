package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.PracticeSubmitDTO;
import com.example.yourcompany.assessment.entity.PracticeRecord;

import java.util.List;

public interface PracticeService {
    PracticeRecord submitPractice(PracticeSubmitDTO submitDTO);
    List<PracticeRecord> getUserPracticeRecords(Integer userId);
    List<PracticeRecord> getUserPracticeRecordsByType(Integer userId, String questionType);
    double calculateUserCorrectRate(Integer userId, String chapter);
    double calculateUserAverageTime(Integer userId, String chapter);
} 