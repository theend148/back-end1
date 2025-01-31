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

    List<AlgorithmQuestionDTO>  getAlgorithmQuestionsByChapter(String chapter);
    List<AlgorithmChapterDTO>  getAllAlgorithmChapter();

    List<KnowledgeChapterDTO>  getAllKnowledgeChapter();


    List<KnowledgeQuestionDTO> getKnowledgeQuestionsByIds(List<Integer> ids);

    List<AlgorithmQuestionDTO> getAlgorithmQuestionsByIds(List<Integer> ids);
}