package com.example.yourcompany.assessment.service;

import java.util.Map;

/**
 * 用户学习进度服务
 */
public interface UserProgressService {
    /**
     * 获取用户的学习进度和掌握情况
     *
     * @param userId 用户ID
     * @return 包含用户学习进度和掌握情况的数据
     */
    Map<String, Object> getUserProgress(Integer userId);
}