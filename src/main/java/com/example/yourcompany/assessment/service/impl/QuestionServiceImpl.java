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


    private final  AlgorithmChapterRepository algorithmChapterRepository;

    private final KnowledgeChapterRepository knowledgeChapterRepository;

    @Override
    public List<KnowledgeQuestionDTO> getAllKnowledgeQuestions() {

        System.out.println("-------getAllKnowledgeQuestions" );
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
        KnowledgeQuestion question = convertToKnowledgeQuestion(questionDTO);
//        System.out.println(question);
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
//        modify
//        KnowledgeQuestionDTO dto = new KnowledgeQuestionDTO();
//        dto.setQuestionId(question.getQuestionId());
//        dto.setChapter(question.getChapter());
//        dto.setQuestionType(question.getQuestionType().toString());
//        dto.setContent(question.getContent());
//        dto.setOptions(question.getOptions() != null ? question.getOptions().split(",") : null);
//
//        dto.setDifficulty(question.getDifficulty().toString());
//
//        return dto;
        KnowledgeQuestionDTO dto = new KnowledgeQuestionDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setChapter(question.getChapter());
        dto.setQuestionType(question.getQuestionType().toString());
        dto.setContent(question.getContent());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        // 将 JSON 字符串反序列化为 String[] 数组
        if (question.getOptions() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 反序列化 JSON 字符串为 String[] 数组
                String[] optionsArray = objectMapper.readValue(question.getOptions(), String[].class);
                dto.setOptions(optionsArray);
            } catch (Exception e) {
                e.printStackTrace();  // 处理反序列化异常
            }
        } else {
            dto.setOptions(null);  // 如果 options 为 null，则设置为 null
        }

        dto.setDifficulty(question.getDifficulty().toString());
        return dto;
    }


//    TODO modify

//    private KnowledgeQuestion convertToKnowledgeQuestion(KnowledgeQuestionDTO dto) {
//        KnowledgeQuestion question = new KnowledgeQuestion();
//        question.setChapter(dto.getChapter());
//        question.setQuestionType(QuestionType.valueOf(dto.getQuestionType()));
//        question.setContent(dto.getContent());
//        question.setCorrectAnswer(dto.getCorrectAnswer());
//        question.setOptions(dto.getOptions() != null ? String.join(",", dto.getOptions()) : null);
//
//        question.setDifficulty(Difficulty.valueOf(dto.getDifficulty()));
//        return question;
//    }
        private KnowledgeQuestion convertToKnowledgeQuestion(KnowledgeQuestionDTO dto) {
            KnowledgeQuestion question = new KnowledgeQuestion();
            question.setChapter(dto.getChapter());
            question.setQuestionType(QuestionType.valueOf(dto.getQuestionType()));
            question.setContent(dto.getContent());
            question.setCorrectAnswer(dto.getCorrectAnswer());

            // 将 options 数组转换为 JSON 格式的字符串
            if (dto.getOptions() != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String optionsJson = objectMapper.writeValueAsString(dto.getOptions());
                    question.setOptions(optionsJson);  // 将 JSON 格式的字符串赋值给 options 字段
                } catch (JsonProcessingException e) {
                    e.printStackTrace();  // 处理序列化异常
                }
            } else {
                question.setOptions(null);  // 如果 options 为 null，则将其设置为 null
            }

            question.setDifficulty(Difficulty.valueOf(dto.getDifficulty()));
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
        System.out.println("convertToAlgorithmQuestionDTO   ----- AlgorithmQuestionDTO："+dto);
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