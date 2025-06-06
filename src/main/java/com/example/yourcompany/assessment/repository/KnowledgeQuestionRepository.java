package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.Difficulty;
import com.example.yourcompany.assessment.entity.KnowledgeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeQuestionRepository extends JpaRepository<KnowledgeQuestion, Integer> {
    List<KnowledgeQuestion> findByChapter(String chapter);

    List<KnowledgeQuestion> findByDifficulty(String difficulty);

    KnowledgeQuestion getByQuestionId(Integer id);

}