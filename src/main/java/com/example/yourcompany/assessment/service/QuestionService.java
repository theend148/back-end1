package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.*;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<KnowledgeQuestionDTO> getAllKnowledgeQuestions();

    List<KnowledgeQuestionDTO> getKnowledgeQuestionsByChapter(String chapter);

    Optional<KnowledgeQuestionDTO> getKnowledgeQuestionById(Integer id);

    KnowledgeQuestionDTO createKnowledgeQuestion(KnowledgeQuestionDTO questionDTO);

    KnowledgeChapterDTO createKnowledgeChapter(KnowledgeChapterDTO questionDTO);

    AlgorithmChapterDTO createAlgorithmChapter(AlgorithmChapterDTO questionDTO);

    List<AlgorithmQuestionDTO> getAllAlgorithmQuestions();

    Optional<AlgorithmQuestionDTO> getAlgorithmQuestionById(Integer id);

    AlgorithmQuestionDTO createAlgorithmQuestion(AddAlgorithmQuestionRequestDTO questionDTO);

    List<AlgorithmQuestionDTO> getAlgorithmQuestionsByChapter(String chapter);

    List<AlgorithmChapterDTO> getAllAlgorithmChapter();

    List<KnowledgeChapterDTO> getAllKnowledgeChapter();

    List<KnowledgeQuestionDTO> getKnowledgeQuestionsByIds(List<Integer> ids);

    List<AlgorithmQuestionDTO> getAlgorithmQuestionsByIds(List<Integer> ids);

    // 更新知识点题目
    KnowledgeQuestionDTO updateKnowledgeQuestion(Integer id, KnowledgeQuestionDTO questionDTO);

    // 更新算法题目
    AlgorithmQuestionDTO updateAlgorithmQuestion(Integer id, AddAlgorithmQuestionRequestDTO questionDTO);

    // 删除知识点题目
    void deleteKnowledgeQuestion(Integer id);

    // 删除算法题目
    void deleteAlgorithmQuestion(Integer id);

    // 更新知识点章节
    KnowledgeChapterDTO updateKnowledgeChapter(Integer id, KnowledgeChapterDTO chapterDTO);

    // 更新算法章节
    AlgorithmChapterDTO updateAlgorithmChapter(Integer id, AlgorithmChapterDTO chapterDTO);

    // 删除知识点章节
    void deleteKnowledgeChapter(Integer id);

    // 删除算法章节
    void deleteAlgorithmChapter(Integer id);

    // 获取算法题目的测试用例
    List<TestCaseDTO> getTestCasesByQuestionId(Integer questionId);
}