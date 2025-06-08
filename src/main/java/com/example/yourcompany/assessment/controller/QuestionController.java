package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.*;
import com.example.yourcompany.assessment.service.QuestionService;
//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    // TODO 知识点的编辑和删除功能没有
    private final QuestionService questionService;

    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeQuestionDTO>> getAllKnowledgeQuestions() {
        System.out.println("getAllKnowledgeQuestions 调用");
        return ResponseEntity.ok(questionService.getAllKnowledgeQuestions());
    }

    @GetMapping("/knowledge/ids")
    public ResponseEntity<List<KnowledgeQuestionDTO>> getKnowledgeQuestionsByIds(@RequestParam List<Integer> ids) {
        return ResponseEntity.ok(questionService.getKnowledgeQuestionsByIds(ids));
    }

    @GetMapping("/algorithm/ids")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAlgorithmQuestionsByIds(@RequestParam List<Integer> ids) {
        return ResponseEntity.ok(questionService.getAlgorithmQuestionsByIds(ids));
    }

    @GetMapping("/chapters/knowledge")
    public ResponseEntity<List<KnowledgeChapterDTO>> getAllKnowledgeChapters() {
        return ResponseEntity.ok(questionService.getAllKnowledgeChapter());
    }

    @GetMapping("/chapters/algorithm")
    public ResponseEntity<List<AlgorithmChapterDTO>> getAllAlgorithmChapters() {
        return ResponseEntity.ok(questionService.getAllAlgorithmChapter());
    }

    @PostMapping("/chapters/knowledge")
    public ResponseEntity<KnowledgeChapterDTO> createKnowledgeChapter(
            @Valid @RequestBody KnowledgeChapterDTO chapterDTO) {

        System.out.println(chapterDTO);
        System.out.println("createKnowledgeChapter 调用");

        return ResponseEntity.ok(questionService.createKnowledgeChapter(chapterDTO));
    }

    @PostMapping("/chapters/algorithm")
    public ResponseEntity<AlgorithmChapterDTO> createAlgorithmChapter(
            @Valid @RequestBody AlgorithmChapterDTO chapterDTO) {

        System.out.println(chapterDTO);
        System.out.println("createKnowledgeChapter 调用");

        return ResponseEntity.ok(questionService.createAlgorithmChapter(chapterDTO));
    }

    @GetMapping("/knowledge/chapter/{chapter}")
    public ResponseEntity<List<KnowledgeQuestionDTO>> getKnowledgeQuestionsByChapter(
            @PathVariable String chapter) {
        System.out.println("getKnowledgeQuestionsByChapter 调用");

        return ResponseEntity.ok(questionService.getKnowledgeQuestionsByChapter(chapter));
    }

    @GetMapping("/knowledge/chapter")
    public void myself1() {
    }

    @GetMapping("/knowledge/{id}")
    public ResponseEntity<KnowledgeQuestionDTO> getKnowledgeQuestion(@PathVariable Integer id) {
        return questionService.getKnowledgeQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/knowledge")
    public ResponseEntity<KnowledgeQuestionDTO> createKnowledgeQuestion(@RequestBody KnowledgeQuestionDTO questionDTO) {
        System.out.println("接收到的知识点题目创建请求: " + questionDTO);
        System.out.println("Content字段内容: " + questionDTO.getContent());
        System.out.println(
                "选项内容: " + (questionDTO.getOptions() != null ? String.join(", ", questionDTO.getOptions()) : "null"));

        return ResponseEntity.ok(questionService.createKnowledgeQuestion(questionDTO));
    }

    @GetMapping("/algorithm")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAllAlgorithmQuestions() {
        return ResponseEntity.ok(questionService.getAllAlgorithmQuestions());
    }

    @GetMapping("/algorithm/chapter/{chapter}")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAlgorithmQuestionsByChapter(@PathVariable String chapter) {
        System.out.println("getAlgorithmQuestionsByChapter 调用");

        return ResponseEntity.ok(questionService.getAlgorithmQuestionsByChapter(chapter));
    }

    @GetMapping("/algorithm/{id}")
    public ResponseEntity<AlgorithmQuestionDTO> getAlgorithmQuestion(@PathVariable Integer id) {
        return questionService.getAlgorithmQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/algorithm")
    public ResponseEntity<AlgorithmQuestionDTO> createAlgorithmQuestion(
            @Valid @RequestBody AddAlgorithmQuestionRequestDTO questionDTO) {

        System.out.println("createAlgorithmQuestion doing --");
        System.out.println(questionDTO);
        return ResponseEntity.ok(questionService.createAlgorithmQuestion(questionDTO));
    }

    @PutMapping("/knowledge/{id}")
    public ResponseEntity<KnowledgeQuestionDTO> updateKnowledgeQuestion(
            @PathVariable Integer id,
            @Valid @RequestBody KnowledgeQuestionDTO questionDTO) {
        System.out.println("更新知识点题目: ID=" + id);
        System.out.println(questionDTO);
        return ResponseEntity.ok(questionService.updateKnowledgeQuestion(id, questionDTO));
    }

    @PutMapping("/algorithm/{id}")
    public ResponseEntity<AlgorithmQuestionDTO> updateAlgorithmQuestion(
            @PathVariable Integer id,
            @Valid @RequestBody AddAlgorithmQuestionRequestDTO questionDTO) {
        System.out.println("更新算法题目: ID=" + id);
        System.out.println(questionDTO);
        return ResponseEntity.ok(questionService.updateAlgorithmQuestion(id, questionDTO));
    }

    @DeleteMapping("/knowledge/{id}")
    public ResponseEntity<?> deleteKnowledgeQuestion(@PathVariable Integer id) {
        try {
            System.out.println("删除知识点题目: ID=" + id);
            questionService.deleteKnowledgeQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("删除知识点题目失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "删除知识点题目失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/algorithm/{id}")
    public ResponseEntity<?> deleteAlgorithmQuestion(@PathVariable Integer id) {
        try {
            System.out.println("删除算法题目: ID=" + id);
            questionService.deleteAlgorithmQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("删除算法题目失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "删除算法题目失败: " + e.getMessage()));
        }
    }

    @GetMapping("/algorithm/{id}/testcases")
    public ResponseEntity<List<TestCaseDTO>> getAlgorithmQuestionTestCases(@PathVariable Integer id) {
        System.out.println("获取算法题目测试用例: ID=" + id);
        return ResponseEntity.ok(questionService.getTestCasesByQuestionId(id));
    }

    @PutMapping("/chapters/knowledge/{id}")
    public ResponseEntity<KnowledgeChapterDTO> updateKnowledgeChapter(
            @PathVariable Integer id,
            @Valid @RequestBody KnowledgeChapterDTO chapterDTO) {
        System.out.println("更新知识点章节: ID=" + id);
        return ResponseEntity.ok(questionService.updateKnowledgeChapter(id, chapterDTO));
    }

    @PutMapping("/chapters/algorithm/{id}")
    public ResponseEntity<AlgorithmChapterDTO> updateAlgorithmChapter(
            @PathVariable Integer id,
            @Valid @RequestBody AlgorithmChapterDTO chapterDTO) {
        System.out.println("更新算法章节: ID=" + id);
        return ResponseEntity.ok(questionService.updateAlgorithmChapter(id, chapterDTO));
    }

    @DeleteMapping("/chapters/knowledge/{id}")
    public ResponseEntity<Void> deleteKnowledgeChapter(@PathVariable Integer id) {
        System.out.println("删除知识点章节: ID=" + id);
        questionService.deleteKnowledgeChapter(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/chapters/algorithm/{id}")
    public ResponseEntity<Void> deleteAlgorithmChapter(@PathVariable Integer id) {
        System.out.println("删除算法章节: ID=" + id);
        questionService.deleteAlgorithmChapter(id);
        return ResponseEntity.noContent().build();
    }
}