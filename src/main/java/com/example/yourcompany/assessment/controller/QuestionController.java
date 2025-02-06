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

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
//    TODO 知识点的编辑和删除功能没有
    private final QuestionService questionService;

    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeQuestionDTO>> getAllKnowledgeQuestions() {
        System.out.println("getAllKnowledgeQuestions 调用");
        return ResponseEntity.ok(questionService.getAllKnowledgeQuestions());
    }

    @GetMapping("/knowledge/ids")
    public ResponseEntity<List<KnowledgeQuestionDTO>> getKnowledgeQuestionsByIds(  @RequestParam List<Integer> ids) {
        return ResponseEntity.ok(questionService.getKnowledgeQuestionsByIds(ids));
    }

    @GetMapping("/algorithm/ids")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAlgorithmQuestionsByIds(  @RequestParam List<Integer> ids) {
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
    public ResponseEntity<KnowledgeQuestionDTO> createKnowledgeQuestion(
            @Valid @RequestBody KnowledgeQuestionDTO questionDTO) {

        System.out.println(questionDTO);
        System.out.println("createKnowledgeQuestion 调用");

        return ResponseEntity.ok(questionService.createKnowledgeQuestion(questionDTO));
    }

    @GetMapping("/algorithm")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAllAlgorithmQuestions() {
        return ResponseEntity.ok(questionService.getAllAlgorithmQuestions());
    }

    @GetMapping("/algorithm/chapter/{chapter}")
    public ResponseEntity<List<AlgorithmQuestionDTO>> getAlgorithmQuestionsByChapter( @PathVariable String chapter) {
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
} 