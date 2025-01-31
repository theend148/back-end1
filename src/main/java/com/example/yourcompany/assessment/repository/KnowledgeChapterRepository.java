package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.AlgorithmChapter;
import com.example.yourcompany.assessment.entity.KnowledgeChapter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Qianyue
 * @Date 2024.12.06 21:43
 **/
public interface KnowledgeChapterRepository extends JpaRepository<KnowledgeChapter, Integer> {

}
