package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.*;
import com.example.yourcompany.assessment.entity.TestRecord;
import com.example.yourcompany.assessment.repository.TestRecordRepository;
import com.example.yourcompany.assessment.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final TestRecordRepository testRecordRepository;

    @GetMapping("")
    public ResponseEntity<List<GetTestResponse>> GetTest(
            @RequestParam Integer page) {
        return ResponseEntity.ok(testService.getAllTests());
    }

    @GetMapping("/{testId}")
    public ResponseEntity<GetTestResponse> GetTestById(
            @PathVariable Integer testId) {
        return ResponseEntity.ok(testService.getTestById(testId));
    }

    @PostMapping("/create")
    public ResponseEntity<Test1DTO> createTest(@Valid @RequestBody CreateTestRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(testService.createTest(request));
    }

    @PutMapping("/{testId}")
    public ResponseEntity<Test1DTO> updateTest(@PathVariable Integer testId, @Valid @RequestBody Test1DTO testDTO) {
        return ResponseEntity.ok(testService.updateTest(testId, testDTO));
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<?> deleteTest(@PathVariable Integer testId) {
        try {
            System.out.println("删除试卷: ID=" + testId);
            testService.deleteTest(testId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("删除试卷失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "删除试卷失败: " + e.getMessage()));
        }
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<TestResultDTO> submitTest(
            @PathVariable Integer testId,
            @RequestBody TestSubmitDTO testSubmit) {
        System.out.println(testSubmit);
        return ResponseEntity.ok(testService.submitTest(testId, testSubmit));
    }

    @PostMapping("/{testId}/submit1")
    public ResponseEntity<TestResult1DTO> submitAlgorithmTest(
            @PathVariable Integer testId,
            @RequestBody TestSubmit1DTO testSubmit) {
        System.out.println(testSubmit);
        return ResponseEntity.ok(testService.submitTest1(testId, testSubmit));
    }

    // @GetMapping("/{testId}/result")
    // public ResponseEntity<TestResultDTO> submitTest(
    // @PathVariable Integer testId) {
    // return ResponseEntity.ok(testService.submitTest(testId, testSubmit));
    // }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestRecord>> getUserTestRecords(@PathVariable Integer userId) {
        List<TestRecord> records = testRecordRepository.findByUserId(userId);
        return ResponseEntity.ok(records);
    }

    // @GetMapping("/user/{userId}/chapter/{chapter}")
    // public ResponseEntity<List<TestRecord>> getUserTestRecordsByChapter(
    // @PathVariable Integer userId,
    // @PathVariable String chapter) {
    // return ResponseEntity.ok(testService.getUserTestRecordsByChapter(userId,
    // chapter));
    // }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getUserTestAverage(@PathVariable Integer userId) {
        return ResponseEntity.ok(testService.calculateUserTestAverage(userId));
    }
}