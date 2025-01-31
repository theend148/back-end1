package com.example.yourcompany.assessment.repository;

import com.example.yourcompany.assessment.entity.AlgorithmQuestion;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmQuestionRepository extends JpaRepository<AlgorithmQuestion, Integer> {

    List<AlgorithmQuestion> findByChapter(String chapter);

    AlgorithmQuestion getByQuestionId(Integer id);

} 