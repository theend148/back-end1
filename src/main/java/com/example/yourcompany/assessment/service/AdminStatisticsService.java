package com.example.yourcompany.assessment.service;

import java.util.List;
import java.util.Map;

/**
 * 管理员统计服务接口，提供各种系统数据统计功能
 */
public interface AdminStatisticsService {
    /**
     * 获取用户统计数据
     * 
     * @return 包含总用户数、新增用户、活跃用户等信息的Map
     */
    Map<String, Object> getUserStatistics();

    /**
     * 获取题目统计数据
     * 
     * @return 包含知识题总数、算法题总数、难度分布和章节分布的Map
     */
    Map<String, Object> getQuestionStatistics();

    /**
     * 获取提交统计数据
     * 
     * @return 包含总提交数、成功率、近期趋势等信息的Map
     */
    Map<String, Object> getSubmissionStatistics();

    /**
     * 获取考试统计数据
     * 
     * @return 包含考试总数、平均分、完成率等信息的Map
     */
    Map<String, Object> getTestStatistics();

    /**
     * 获取章节详细统计数据
     * 
     * @return 包含各章节详细统计信息的列表
     */
    List<Map<String, Object>> getChapterDetailedStatistics();

    /**
     * 获取用户详细统计数据
     * 
     * @return 包含用户详细统计信息的列表
     */
    List<Map<String, Object>> getUserDetailedStatistics();

    /**
     * 获取提交详细统计数据
     * 
     * @return 包含提交详细统计信息的列表
     */
    List<Map<String, Object>> getSubmissionDetailedStatistics();

    /**
     * 获取考试详细统计数据
     * 
     * @return 包含考试详细统计信息的列表
     */
    List<Map<String, Object>> getTestDetailedStatistics();
}
