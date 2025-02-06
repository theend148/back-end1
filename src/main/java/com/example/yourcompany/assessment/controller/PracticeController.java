package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.PracticeSubmitDTO;
import com.example.yourcompany.assessment.entity.PracticeRecord;
import com.example.yourcompany.assessment.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class PracticeController {
    private final PracticeService practiceService;

    @PostMapping("/submit")
    public ResponseEntity<PracticeRecord> submitPractice(@Valid @RequestBody PracticeSubmitDTO submitDTO) {
        System.out.println(submitDTO);
        return ResponseEntity.ok(practiceService.submitPractice(submitDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeRecord>> getUserPracticeRecords(@PathVariable Integer userId) {
        return ResponseEntity.ok(practiceService.getUserPracticeRecords(userId));
    }

    @GetMapping("/chapter/{userId}")
    public ResponseEntity<List<PracticeRecord>> todefuc1(@PathVariable Integer userId) {
//        return ResponseEntity.ok(practiceService.getUserPracticeRecords(userId));
        return null;
    }

    @GetMapping("/user/{userId}/type/{questionType}")
    public ResponseEntity<List<PracticeRecord>> getUserPracticeRecordsByType(
            @PathVariable Integer userId,
            @PathVariable String questionType) {
        return ResponseEntity.ok(practiceService.getUserPracticeRecordsByType(userId, questionType));
    }

    @GetMapping("/user/{userId}/rate/{chapter}")
    public ResponseEntity<Double> getUserCorrectRate(
            @PathVariable Integer userId,
            @PathVariable String chapter) {
        return ResponseEntity.ok(practiceService.calculateUserCorrectRate(userId, chapter));
    }
} 