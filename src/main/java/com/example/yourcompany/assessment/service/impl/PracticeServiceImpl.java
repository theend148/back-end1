package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.repository.PracticeRecordRepository;
import com.example.yourcompany.assessment.repository.UserRepository;
import com.example.yourcompany.assessment.service.PracticeService;
import com.example.yourcompany.assessment.dto.PracticeSubmitDTO;
import com.example.yourcompany.assessment.entity.PracticeRecord;
import com.example.yourcompany.assessment.entity.User;
//import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeServiceImpl implements PracticeService {
    private final PracticeRecordRepository practiceRecordRepository;
    private final UserRepository userRepository;

    @Override
    public PracticeRecord submitPractice(PracticeSubmitDTO submitDTO) {
        User user = userRepository.findById(submitDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PracticeRecord record = new PracticeRecord();
        record.setUser(user);
        record.setQuestionId(submitDTO.getQuestionId());
        record.setUserAnswer(submitDTO.getAnswer());
        record.setIsCorrect(submitDTO.getIsCorrect());
        record.setQuestionType(submitDTO.getQuestionType());
        return practiceRecordRepository.save(record);
    }

    @Override
    public List<PracticeRecord> getUserPracticeRecords(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return practiceRecordRepository.findByUser(user);
    }

    @Override
    public List<PracticeRecord> getUserPracticeRecordsByType(Integer userId, String questionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return practiceRecordRepository.findByUserAndQuestionType(user, questionType);
    }

    @Override
    public double calculateUserCorrectRate(Integer userId, String chapter) {
        List<PracticeRecord> records = getUserPracticeRecords(userId);
        if (records.isEmpty()) {
            return 0.0;
        }

        long correctCount = records.stream()
                .filter(PracticeRecord::getIsCorrect)
                .count();

        return (double) correctCount / records.size();
    }

    @Override
    public double calculateUserAverageTime(Integer userId, String chapter) {
        // 这里需要添加计算平均时间的逻辑
        // 可能需要在PracticeRecord中添加开始时间和结束时间字段
        return 0.0;
    }
} 