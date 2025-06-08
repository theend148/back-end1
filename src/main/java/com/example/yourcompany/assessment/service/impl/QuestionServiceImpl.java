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
import org.springframework.transaction.annotation.Transactional;

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
    private final SubmissionRecordRepository submissionRecordRepository;
    private final TestAnswerDetailRepository testAnswerDetailRepository;

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

    @Override
    public KnowledgeQuestionDTO updateKnowledgeQuestion(Integer id, KnowledgeQuestionDTO questionDTO) {
        // 检查题目是否存在
        KnowledgeQuestion existingQuestion = knowledgeQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("知识点题目不存在: " + id));

        // 更新题目信息
        existingQuestion.setChapter(questionDTO.getChapter());
        existingQuestion.setContent(questionDTO.getContent());
        existingQuestion.setCorrectAnswer(questionDTO.getCorrectAnswer());

        // 处理难度枚举
        try {
            existingQuestion.setDifficulty(Difficulty.valueOf(questionDTO.getDifficulty().toLowerCase()));
        } catch (IllegalArgumentException e) {
            existingQuestion.setDifficulty(Difficulty.easy);
        }

        // 处理问题范围枚举
        try {
            if (questionDTO.getQuestionScope() != null) {
                existingQuestion.setQuestionScope(QuestionScope.valueOf(questionDTO.getQuestionScope().toLowerCase()));
            } else {
                existingQuestion.setQuestionScope(QuestionScope.practice);
            }
        } catch (IllegalArgumentException e) {
            existingQuestion.setQuestionScope(QuestionScope.practice);
        }

        // 处理题目类型枚举
        try {
            existingQuestion.setQuestionType(QuestionType.valueOf(questionDTO.getQuestionType().toLowerCase()));
        } catch (IllegalArgumentException e) {
            existingQuestion.setQuestionType(QuestionType.choice);
        }

        // 处理选项JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            existingQuestion.setOptions(mapper.writeValueAsString(questionDTO.getOptions()));
        } catch (JsonProcessingException e) {
            existingQuestion.setOptions("[]");
        }

        // 保存更新后的题目
        KnowledgeQuestion updatedQuestion = knowledgeQuestionRepository.save(existingQuestion);
        return convertToKnowledgeQuestionDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public AlgorithmQuestionDTO updateAlgorithmQuestion(Integer id, AddAlgorithmQuestionRequestDTO questionDTO) {
        // 检查题目是否存在
        AlgorithmQuestion existingQuestion = algorithmQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("算法题目不存在: " + id));

        // 打印接收到的数据，帮助调试
        System.out.println("更新算法题目: ID=" + id);
        System.out
                .println("接收到的测试用例数量: " + (questionDTO.getTestCases() != null ? questionDTO.getTestCases().size() : 0));

        // 更新题目基本信息
        existingQuestion.setChapter(questionDTO.getChapter());
        existingQuestion.setTitle(questionDTO.getTitle());
        existingQuestion.setDescription(questionDTO.getDescription());
        existingQuestion.setInputFormat(questionDTO.getInputFormat());
        existingQuestion.setOutputFormat(questionDTO.getOutputFormat());
        existingQuestion.setSampleInput(questionDTO.getSampleInput());
        existingQuestion.setSampleOutput(questionDTO.getSampleOutput());
        existingQuestion.setConstraints(questionDTO.getConstraints());

        // 处理难度枚举
        try {
            existingQuestion.setDifficulty(Difficulty.valueOf(questionDTO.getDifficulty().toLowerCase()));
        } catch (IllegalArgumentException e) {
            existingQuestion.setDifficulty(Difficulty.easy);
        }

        // 处理问题范围枚举
        try {
            if (questionDTO.getQuestionScope() != null) {
                existingQuestion.setQuestionScope(QuestionScope.valueOf(questionDTO.getQuestionScope().toLowerCase()));
            } else {
                existingQuestion.setQuestionScope(QuestionScope.practice);
            }
        } catch (IllegalArgumentException e) {
            existingQuestion.setQuestionScope(QuestionScope.practice);
        }

        // 保存更新后的题目
        AlgorithmQuestion updatedQuestion = algorithmQuestionRepository.save(existingQuestion);

        // 处理测试用例
        if (questionDTO.getTestCases() != null && !questionDTO.getTestCases().isEmpty()) {
            System.out.println("更新测试用例...");
            // 删除旧的测试用例
            testCaseRepository.deleteByQuestionQuestionId(id);

            // 添加新的测试用例
            List<TestCase> testCases = questionDTO.getTestCases().stream()
                    .map(testCaseDTO -> {
                        TestCase testCase = new TestCase();
                        testCase.setQuestion(updatedQuestion);
                        testCase.setInputData(testCaseDTO.getInput());
                        testCase.setOutputData(testCaseDTO.getOutput());
                        testCase.setPublic(testCaseDTO.isPublic());
                        return testCase;
                    })
                    .collect(Collectors.toList());
            testCaseRepository.saveAll(testCases);
            System.out.println("保存了 " + testCases.size() + " 个测试用例");
        } else {
            System.out.println("没有测试用例需要更新");
        }

        return convertToAlgorithmQuestionDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteKnowledgeQuestion(Integer id) {
        try {
            // 检查题目是否存在
            if (!knowledgeQuestionRepository.existsById(id)) {
                System.out.println("知识点题目不存在: " + id);
                return;
            }

            System.out.println("开始删除知识点题目: " + id);
            knowledgeQuestionRepository.deleteById(id);
            System.out.println("知识点题目删除完成: " + id);
        } catch (Exception e) {
            System.err.println("删除知识点题目时发生错误: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常以便事务回滚
        }
    }

    @Override
    @Transactional
    public void deleteAlgorithmQuestion(Integer id) {
        try {
            // 检查题目是否存在
            if (!algorithmQuestionRepository.existsById(id)) {
                System.out.println("算法题不存在: " + id);
                return;
            }

            // 先删除相关的TestAnswerDetail记录
            System.out.println("开始删除算法题的答题记录: " + id);
            testAnswerDetailRepository.deleteByQuestionId(id);
            System.out.println("答题记录删除完成");

            // 再删除相关的提交记录
            System.out.println("开始删除算法题的提交记录: " + id);
            submissionRecordRepository.deleteByQuestionQuestionId(id);
            System.out.println("提交记录删除完成");

            System.out.println("开始删除算法题的测试用例: " + id);
            // 再删除相关的测试用例
            testCaseRepository.deleteByQuestionQuestionId(id);
            System.out.println("测试用例删除完成，开始删除算法题: " + id);

            // 最后删除题目
            algorithmQuestionRepository.deleteById(id);
            System.out.println("算法题删除完成: " + id);
        } catch (Exception e) {
            System.err.println("删除算法题时发生错误: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常以便事务回滚
        }
    }

    @Override
    public KnowledgeChapterDTO updateKnowledgeChapter(Integer id, KnowledgeChapterDTO chapterDTO) {
        // 检查章节是否存在
        KnowledgeChapter existingChapter = knowledgeChapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("知识点章节不存在: " + id));

        // 更新章节信息
        existingChapter.setChapter(chapterDTO.getChapter());

        // 保存更新后的章节
        KnowledgeChapter updatedChapter = knowledgeChapterRepository.save(existingChapter);
        return convertToKnowledgeChapterDTO(updatedChapter);
    }

    @Override
    public AlgorithmChapterDTO updateAlgorithmChapter(Integer id, AlgorithmChapterDTO chapterDTO) {
        // 检查章节是否存在
        AlgorithmChapter existingChapter = algorithmChapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("算法章节不存在: " + id));

        // 更新章节信息
        existingChapter.setChapter(chapterDTO.getChapter());

        // 保存更新后的章节
        AlgorithmChapter updatedChapter = algorithmChapterRepository.save(existingChapter);
        return convertToAlgorithmChapterDTO(updatedChapter);
    }

    @Override
    public void deleteKnowledgeChapter(Integer id) {
        knowledgeChapterRepository.deleteById(id);
    }

    @Override
    public void deleteAlgorithmChapter(Integer id) {
        algorithmChapterRepository.deleteById(id);
    }

    @Override
    public List<TestCaseDTO> getTestCasesByQuestionId(Integer questionId) {
        List<TestCase> testCases = testCaseRepository.findByQuestionQuestionId(questionId);
        return testCases.stream()
                .map(this::convertToTestCaseDTO)
                .collect(Collectors.toList());
    }

    private TestCaseDTO convertToTestCaseDTO(TestCase testCase) {
        TestCaseDTO dto = new TestCaseDTO();
        dto.setCaseId(testCase.getCaseId());
        dto.setInputData(testCase.getInputData());
        dto.setOutputData(testCase.getOutputData());
        dto.setPublic(testCase.isPublic());
        return dto;
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