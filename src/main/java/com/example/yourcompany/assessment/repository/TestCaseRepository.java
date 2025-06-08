package com.example.yourcompany.assessment.repository;

/**
 * @author Qianyue
 * @Date 2025.01.18 21:29
 **/
import com.example.yourcompany.assessment.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    // 根据题目ID查找所有测试用例
    List<TestCase> findByQuestionQuestionId(Integer questionId);

    // 根据题目ID查找公开的测试用例
    List<TestCase> findByQuestionQuestionIdAndIsPublicTrue(Integer questionId);

    // 根据题目ID查找私有的测试用例
    List<TestCase> findByQuestionQuestionIdAndIsPublicFalse(Integer questionId);

    // 删除题目的所有测试用例
    @Modifying
    @Transactional
    void deleteByQuestionQuestionId(Integer questionId);

    // 统计题目的测试用例数量
    long countByQuestionQuestionId(Integer questionId);

    // 自定义查询：获取题目的所有测试用例输入输出
    @Query("SELECT tc FROM TestCase tc WHERE tc.question.questionId = :questionId")
    List<TestCase> findTestCasesWithIO(@Param("questionId") Integer questionId);

    // 批量保存测试用例
    @Override
    <S extends TestCase> List<S> saveAll(Iterable<S> entities);
}
