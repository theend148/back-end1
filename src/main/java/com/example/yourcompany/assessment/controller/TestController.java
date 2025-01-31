package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.*;
import com.example.yourcompany.assessment.entity.TestRecord;
import com.example.yourcompany.assessment.service.TestService;
import com.example.yourcompany.assessment.service.TestService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

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
    public ResponseEntity<Test1DTO> updateTest(@PathVariable Integer testId,@Valid @RequestBody Test1DTO testDTO) {
        return ResponseEntity.ok(testService.updateTest(testId, testDTO));
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<TestResultDTO> submitTest(
            @PathVariable Integer testId,
            @RequestBody TestSubmitDTO testSubmit) {
        System.out.println(testSubmit);
        return ResponseEntity.ok(testService.submitTest(testId, testSubmit));
    }

//    @GetMapping("/{testId}/result")
//    public ResponseEntity<TestResultDTO> submitTest(
//            @PathVariable Integer testId) {
//        return ResponseEntity.ok(testService.submitTest(testId, testSubmit));
//    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestRecord>> getUserTestRecords(@PathVariable Integer userId) {
        return ResponseEntity.ok(null);
    }

//    @GetMapping("/user/{userId}/chapter/{chapter}")
//    public ResponseEntity<List<TestRecord>> getUserTestRecordsByChapter(
//            @PathVariable Integer userId,
//            @PathVariable String chapter) {
//        return ResponseEntity.ok(testService.getUserTestRecordsByChapter(userId, chapter));
//    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getUserTestAverage(@PathVariable Integer userId) {
        return ResponseEntity.ok(testService.calculateUserTestAverage(userId));
    }
} 