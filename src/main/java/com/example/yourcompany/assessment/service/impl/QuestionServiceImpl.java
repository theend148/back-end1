package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.dto.*;
import com.example.yourcompany.assessment.entity.*;
import com.example.yourcompany.assessment.repository.*;
import com.example.yourcompany.assessment.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final KnowledgeQuestionRepository knowledgeQuestionRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;
    private final TestCaseRepository testCaseRepository;

    private final AlgorithmChapterRepository algorithmChapterRepository;

    private final KnowledgeChapterRepository knowledgeChapterRepository;

    @Override
    public List<KnowledgeQuestionDTO> getAllKnowledgeQuestions() {

        System.out.println("-------getAllKnowledgeQuestions");
        return knowledgeQuestionRepository.findAll().stream()
                .map(this::convertToKnowledgeQuestionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeQuestionDTO> getKnowledgeQuestionsByChapter(String chapter) {
        return knowledgeQuestionRepository.findByChapter(chapter).stream()
                .map(this::convertToKnowledgeQuestionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<KnowledgeQuestionDTO> getKnowledgeQuestionById(Integer id) {
        return knowledgeQuestionRepository.findById(id)
                .map(this::convertToKnowledgeQuestionDTO);
    }

    @Override
    public KnowledgeQuestionDTO createKnowledgeQuestion(KnowledgeQuestionDTO questionDTO) {
        // 添加日志记录提交的数据
        System.out.println("Creating knowledge question with data: " + questionDTO);

        // 验证content字段
        if (questionDTO.getContent() == null || questionDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("题目内容(content)不能为空");
        }

        KnowledgeQuestion question = convertToKnowledgeQuestion(questionDTO);
        return convertToKnowledgeQuestionDTO(knowledgeQuestionRepository.save(question));
    }

    @Override
    public KnowledgeChapterDTO createKnowledgeChapter(KnowledgeChapterDTO questionDTO) {
        KnowledgeChapter obj = convertToKnowledgeChapter(questionDTO);
        return convertToKnowledgeChapterDTO(knowledgeChapterRepository.save(obj));

    }

    @Override
    public AlgorithmChapterDTO createAlgorithmChapter(AlgorithmChapterDTO questionDTO) {
        AlgorithmChapter obj = convertToAlgorithmChapter(questionDTO);

        return convertToAlgorithmChapterDTO(algorithmChapterRepository.save(obj));

    }

    @Override
    public List<AlgorithmQuestionDTO> getAllAlgorithmQuestions() {
        return algorithmQuestionRepository.findAll().stream()
                .map(this::convertToAlgorithmQuestionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AlgorithmQuestionDTO> getAlgorithmQuestionById(Integer id) {
        System.out.println("getAlgorithmQuestionById  调用");
        return algorithmQuestionRepository.findById(id)
                .map(this::convertToAlgorithmQuestionDTO);
    }

    @Override
    public AlgorithmQuestionDTO createAlgorithmQuestion(AddAlgorithmQuestionRequestDTO questionDTO) {
        AlgorithmQuestion question = convertToAlgorithmQuestion(questionDTO);
        AlgorithmQuestion savedQuestion = algorithmQuestionRepository.save(question);

        if (questionDTO.getTestCases() != null && !questionDTO.getTestCases().isEmpty()) {
            List<TestCase> testCases = questionDTO.getTestCases().stream()
                    .map(testCaseDTO -> {
                        TestCase testCase = new TestCase();
                        testCase.setQuestion(savedQuestion);
                        testCase.setInputData(testCaseDTO.getInput());
                        testCase.setOutputData(testCaseDTO.getOutput());
                        testCase.setPublic(testCaseDTO.isPublic());
                        return testCase;
                    })
                    .collect(Collectors.toList());
            testCaseRepository.saveAll(testCases);
        }
        return convertToAlgorithmQuestionDTO(algorithmQuestionRepository.save(question));
    }

    @Override
    public List<AlgorithmQuestionDTO> getAlgorithmQuestionsByChapter(String chapter) {
        return algorithmQuestionRepository.findByChapter(chapter).stream()
                .map(this::convertToAlgorithmQuestionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlgorithmChapterDTO> getAllAlgorithmChapter() {
        return algorithmChapterRepository.findAll().stream()
                .map(this::convertToAlgorithmChapterDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeChapterDTO> getAllKnowledgeChapter() {
        return knowledgeChapterRepository.findAll().stream()
                .map(this::convertToKnowledgeChapterDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeQuestionDTO> getKnowledgeQuestionsByIds(List<Integer> ids) {
        List<KnowledgeQuestionDTO> ans = new ArrayList<>();
        for (Integer id : ids) {
            knowledgeQuestionRepository.findById(id)
                    .map(this::convertToKnowledgeQuestionDTO) // 转换为 DTO
                    .ifPresent(ans::add); // 如果存在，添加到结果列表中
        }
        return ans;
    }

    @Override
    public List<AlgorithmQuestionDTO> getAlgorithmQuestionsByIds(List<Integer> ids) {
        List<AlgorithmQuestionDTO> ans = new ArrayList<>();
        for (Integer id : ids) {
            algorithmQuestionRepository.findById(id)
                    .map(this::convertToAlgorithmQuestionDTO) // 转换为 DTO
                    .ifPresent(ans::add); // 如果存在，添加到结果列表中
        }
        return ans;
    }

    private KnowledgeQuestionDTO convertToKnowledgeQuestionDTO(KnowledgeQuestion question) {
        System.out.println("convertToKnowledgeQuestionDTO   ----- KnowledgeQuestionDTO：" + question);
        KnowledgeQuestionDTO dto = new KnowledgeQuestionDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setChapter(question.getChapter());
        dto.setQuestionType(question.getQuestionType() != null ? question.getQuestionType().toString() : "choice");

        // 处理content字段可能为null的情况
        dto.setContent(question.getContent() != null ? question.getContent() : "");

        // 处理correctAnswer字段可能为null的情况
        dto.setCorrectAnswer(question.getCorrectAnswer() != null ? question.getCorrectAnswer() : "");

        // 处理difficulty字段可能为null的情况
        dto.setDifficulty(question.getDifficulty() != null ? question.getDifficulty().toString() : "easy");

        // 处理questionScope字段可能为null的情况
        dto.setQuestionScope(question.getQuestionScope() != null ? question.getQuestionScope().toString() : "practice");

        ObjectMapper mapper = new ObjectMapper();
        try {
            if (question.getOptions() != null) {
                dto.setOptions(mapper.readValue(question.getOptions(), String[].class));
            } else {
                dto.setOptions(new String[0]);
            }
        } catch (JsonProcessingException e) {
            // 处理异常...
            dto.setOptions(new String[0]);
        }

        return dto;
    }

    private KnowledgeQuestion convertToKnowledgeQuestion(KnowledgeQuestionDTO dto) {
        KnowledgeQuestion question = new KnowledgeQuestion();
        question.setChapter(dto.getChapter());
        question.setContent(dto.getContent());
        question.setCorrectAnswer(dto.getCorrectAnswer());

        // 处理难度枚举
        try {
            question.setDifficulty(Difficulty.valueOf(dto.getDifficulty().toLowerCase()));
        } catch (IllegalArgumentException e) {
            // 如果传入的难度值无效，设置默认值
            question.setDifficulty(Difficulty.easy);
        }

        // 处理问题范围枚举
        try {
            if (dto.getQuestionScope() != null) {
                question.setQuestionScope(QuestionScope.valueOf(dto.getQuestionScope().toLowerCase()));
            } else {
                question.setQuestionScope(QuestionScope.practice);
            }
        } catch (IllegalArgumentException e) {
            // 如果传入的问题范围值无效，设置默认值
            question.setQuestionScope(QuestionScope.practice);
        }

        // 处理题目类型枚举
        try {
            question.setQuestionType(QuestionType.valueOf(dto.getQuestionType().toLowerCase()));
        } catch (IllegalArgumentException e) {
            // 如果传入的题目类型值无效，设置默认值
            question.setQuestionType(QuestionType.choice);
        }

        // 处理选项JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            question.setOptions(mapper.writeValueAsString(dto.getOptions()));
        } catch (JsonProcessingException e) {
            // 处理异常...
            question.setOptions("[]");
        }

        return question;
    }

    private AlgorithmQuestionDTO convertToAlgorithmQuestionDTO(AlgorithmQuestion question) {
        AlgorithmQuestionDTO dto = new AlgorithmQuestionDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setTitle(question.getTitle());
        dto.setDescription(question.getDescription());
        dto.setInputFormat(question.getInputFormat());
        dto.setOutputFormat(question.getOutputFormat());
        dto.setSampleInput(question.getSampleInput());
        dto.setSampleOutput(question.getSampleOutput());
        dto.setConstraints(question.getConstraints());
        dto.setChapter(question.getChapter());
        dto.setDifficulty(question.getDifficulty().toString());
        dto.setQuestionScope(question.getQuestionScope().toString());
        System.out.println("convertToAlgorithmQuestionDTO   ----- AlgorithmQuestionDTO：" + dto);
        return dto;
    }

    private AlgorithmQuestion convertToAlgorithmQuestion(AddAlgorithmQuestionRequestDTO questionDTO) {
        AlgorithmQuestion question = new AlgorithmQuestion();

        // 设置基本字段
        question.setChapter(questionDTO.getChapter());
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setInputFormat(questionDTO.getInputFormat());
        question.setOutputFormat(questionDTO.getOutputFormat());
        question.setSampleInput(questionDTO.getSampleInput());
        question.setSampleOutput(questionDTO.getSampleOutput());
        question.setConstraints(questionDTO.getConstraints());

        // 处理难度枚举
        try {
            question.setDifficulty(Difficulty.valueOf(questionDTO.getDifficulty().toLowerCase()));
        } catch (IllegalArgumentException e) {
            // 如果传入的难度值无效，设置默认值
            question.setDifficulty(Difficulty.easy);
        }

        // 处理问题范围枚举
        try {
            if (questionDTO.getQuestionScope() != null) {
                question.setQuestionScope(QuestionScope.valueOf(questionDTO.getQuestionScope().toLowerCase()));
            } else {
                question.setQuestionScope(QuestionScope.practice);
            }
        } catch (IllegalArgumentException e) {
            // 如果传入的问题范围值无效，设置默认值
            question.setQuestionScope(QuestionScope.practice);
        }

        return question;
    }

    private AlgorithmChapterDTO convertToAlgorithmChapterDTO(AlgorithmChapter question) {
        AlgorithmChapterDTO dto = new AlgorithmChapterDTO();
        dto.setChapterId(question.getChapterId());
        dto.setChapter(question.getChapter());
        return dto;
    }

    private AlgorithmChapter convertToAlgorithmChapter(AlgorithmChapterDTO dto) {
        AlgorithmChapter question = new AlgorithmChapter();
        question.setChapter(dto.getChapter());
        return question;
    }

    private KnowledgeChapterDTO convertToKnowledgeChapterDTO(KnowledgeChapter question) {
        KnowledgeChapterDTO dto = new KnowledgeChapterDTO();
        dto.setChapterId(question.getChapterId());
        dto.setChapter(question.getChapter());
        return dto;
    }

    private KnowledgeChapter convertToKnowledgeChapter(KnowledgeChapterDTO dto) {
        KnowledgeChapter question = new KnowledgeChapter();
        question.setChapter(dto.getChapter());
        return question;
    }
}