package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.service.UserProgressService;
import com.example.yourcompany.assessment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户学习进度控制器
 */
@RestController
@RequestMapping("/user/progress")
@RequiredArgsConstructor
public class UserProgressController {
    private final UserProgressService userProgressService;
    private final UserService userService;

    /**
     * 获取用户的学习进度和掌握情况
     *
     * @param userId  用户ID（可选，如果不提供则使用当前登录用户）
     * @param request HTTP请求
     * @return 用户学习进度和掌握情况数据
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProgress(
            @PathVariable Integer userId,
            HttpServletRequest request) {
        // 如果没有提供用户ID，则使用当前登录用户的ID
        // Integer currentUserId = null;

        /* try {
            // 从请求中获取当前用户ID（具体实现可能需要根据实际的身份验证机制调整）
            String username = (String) request.getAttribute("username");
            if (username != null) {
                currentUserId = userService.getUserByUsername(username)
                        .map(userDTO -> userDTO.getUserId())
                        .orElse(null);
            }
        } catch (Exception e) {
            // 处理错误
        } */

        // 如果提供了用户ID，则使用它；否则使用当前用户ID
        Integer targetUserId = userId;

        // 如果都没有，则返回未授权错误
        if (targetUserId == null) {
            return ResponseEntity.status(401).build(); // 未授权
        }

        // 获取用户进度数据
        Map<String, Object> progressData = userProgressService.getUserProgress(targetUserId);

        //打印获取到的用户进度数据
        System.out.println("获取到的用户进度数据");
        System.out.println(progressData);
        return ResponseEntity.ok(progressData);
    }
}