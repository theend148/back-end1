package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.*;

import java.util.List;

public interface TestService {
    // TestRecord createTest(Integer userId, String chapter);
    // TestRecord submitTest(Integer testId, List<String> answers);
    // List<TestRecord> getUserTestRecords(Integer userId);
    // List<TestRecord> getUserTestRecordsByChapter(Integer userId, String chapter);
    double calculateUserTestAverage(Integer userId);

    double calculateChapterTestAverage(String chapter);

    Test1DTO createTest(CreateTestRequest obj);

    List<GetTestResponse> getAllTests();

    Test1DTO updateTest(Integer testId, Test1DTO testDTO);

    GetTestResponse getTestById(Integer testId);

    TestResultDTO submitTest(Integer testId, TestSubmitDTO testSubmit);

    TestResult1DTO submitTest1(Integer testId, TestSubmit1DTO testSubmit);

    TestResultDTO getTestResult(Integer testId);

    // 删除试卷
    void deleteTest(Integer testId);
}