package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.service.StatisticsService;
import com.example.yourcompany.assessment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UserRepository userRepository;
    private final KnowledgeQuestionRepository knowledgeQuestionRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;
    private final PracticeRecordRepository practiceRecordRepository;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final JdbcTemplate jdbcTemplate;

    private Long convertToLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        return 0L;
    }

    private Double convertToDouble(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        return 0.0;
    }

    @Override
    public int getTotalUsers() {
        return (int) userRepository.count();
    }

    @Override
    public int getTotalQuestions() {
        return (int) (knowledgeQuestionRepository.count() + algorithmQuestionRepository.count());
    }

    @Override
    public int getTotalSubmissions() {
        return (int) (practiceRecordRepository.count() + codeSubmissionRepository.count());
    }

    @Override
    public Map<String, Object> getUserActivityStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);
        LocalDate monthAgo = today.minusMonths(1);

        Map<String, Object> result = new HashMap<>();
        
        result.put("daily", getActivityStats(today));
        result.put("weekly", getActivityStats(weekAgo));
        result.put("monthly", getActivityStats(monthAgo));
        
        return result;
    }

    private Map<String, Object> getActivityStats(LocalDate date) {
        String sql = """
            SELECT 
                COUNT(DISTINCT user_id) as active_users,
                COUNT(*) as submissions
            FROM (
                SELECT user_id FROM practice_records 
                WHERE DATE(submitted_at) >= ?
                UNION ALL
                SELECT user_id FROM code_submissions 
                WHERE DATE(submission_time) >= ?
            ) combined
        """;
        
        Map<String, Object> stats = jdbcTemplate.queryForMap(sql, date, date);
        return Map.of(
                "activeUsers", convertToLong(stats.get("active_users")),
                "submissions", convertToLong(stats.get("submissions")));
    }

    @Override
    public Map<String, Object> getChapterStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        String knowledgeSql = """
            SELECT 
                k.chapter,
                COUNT(DISTINCT pr.user_id) as active_users,
                COUNT(*) as total_attempts,
                SUM(CASE WHEN pr.is_correct THEN 1 ELSE 0 END) as correct_attempts
            FROM chapter_knowledge k
            LEFT JOIN knowledge_questions kq ON k.chapter = kq.chapter
            LEFT JOIN practice_records pr ON kq.question_id = pr.question_id
            GROUP BY k.chapter
        """;
        
        List<Map<String, Object>> knowledgeStats = jdbcTemplate.queryForList(knowledgeSql);
        
        String algorithmSql = """
            SELECT 
                a.chapter,
                COUNT(DISTINCT cs.user_id) as active_users,
                COUNT(*) as total_attempts,
                SUM(CASE WHEN cs.result = 'correct' THEN 1 ELSE 0 END) as correct_attempts
            FROM chapter_algorithm a
            LEFT JOIN algorithm_questions aq ON a.chapter = aq.chapter
            LEFT JOIN code_submissions cs ON aq.question_id = cs.question_id
            GROUP BY a.chapter
        """;
        
        List<Map<String, Object>> algorithmStats = jdbcTemplate.queryForList(algorithmSql);
        
        for (Map<String, Object> stat : knowledgeStats) {
            String chapter = (String) stat.get("chapter");
            long totalAttempts = convertToLong(stat.get("total_attempts"));
            long correctAttempts = convertToLong(stat.get("correct_attempts"));
            double avgCorrectRate = totalAttempts > 0 ? (double) correctAttempts / totalAttempts : 0;
            
            result.put(chapter, Map.of(
                    "activeUsers", convertToLong(stat.get("active_users")),
                    "avgCorrectRate", avgCorrectRate));
        }
        
        for (Map<String, Object> stat : algorithmStats) {
            String chapter = (String) stat.get("chapter");
            long totalAttempts = convertToLong(stat.get("total_attempts"));
            long correctAttempts = convertToLong(stat.get("correct_attempts"));
            double avgCorrectRate = totalAttempts > 0 ? (double) correctAttempts / totalAttempts : 0;
            
            result.put(chapter, Map.of(
                    "activeUsers", convertToLong(stat.get("active_users")),
                    "avgCorrectRate", avgCorrectRate));
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getDifficultyDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        String knowledgeSql = """
            SELECT 
                difficulty,
                COUNT(*) as total_questions,
                COUNT(DISTINCT pr.user_id) as active_users,
                SUM(CASE WHEN pr.is_correct THEN 1 ELSE 0 END) as correct_attempts
            FROM knowledge_questions kq
            LEFT JOIN practice_records pr ON kq.question_id = pr.question_id
            GROUP BY difficulty
        """;
        
        String algorithmSql = """
            SELECT 
                difficulty,
                COUNT(*) as total_questions,
                COUNT(DISTINCT cs.user_id) as active_users,
                SUM(CASE WHEN cs.result = 'correct' THEN 1 ELSE 0 END) as correct_attempts
            FROM algorithm_questions aq
            LEFT JOIN code_submissions cs ON aq.question_id = cs.question_id
            GROUP BY difficulty
        """;
        
        Map<String, Map<String, Object>> difficultyStats = new HashMap<>();
        
        jdbcTemplate.queryForList(knowledgeSql).forEach(stat -> {
            String difficulty = (String) stat.get("difficulty");
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalQuestions", convertToLong(stat.get("total_questions")));
            stats.put("activeUsers", convertToLong(stat.get("active_users")));
            stats.put("correctAttempts", convertToLong(stat.get("correct_attempts")));
            difficultyStats.put(difficulty, stats);
        });

        jdbcTemplate.queryForList(algorithmSql).forEach(stat -> {
            String difficulty = (String) stat.get("difficulty");
            Map<String, Object> existingStats = difficultyStats.computeIfAbsent(difficulty, k -> new HashMap<>());
            
            long totalQuestions = convertToLong(existingStats.get("totalQuestions")) +
                    convertToLong(stat.get("total_questions"));
            long activeUsers = convertToLong(existingStats.get("activeUsers")) +
                    convertToLong(stat.get("active_users"));
            long correctAttempts = convertToLong(existingStats.get("correctAttempts")) +
                    convertToLong(stat.get("correct_attempts"));

            existingStats.put("totalQuestions", totalQuestions);
            existingStats.put("activeUsers", activeUsers);
            existingStats.put("correctAttempts", correctAttempts);
        });
        
        result.putAll(difficultyStats);
        return result;
    }

    @Override
    public Map<String, Object> getSubmissionTrend() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        
        String sql = """
            SELECT 
                DATE(submission_time) as date,
                COUNT(*) as submissions,
                SUM(CASE WHEN result = 'correct' THEN 1 ELSE 0 END) as correct_submissions
            FROM (
                SELECT submitted_at as submission_time, 
                       CASE WHEN is_correct THEN 'correct' ELSE 'wrong' END as result
                FROM practice_records
                UNION ALL
                SELECT submission_time, result
                FROM code_submissions
            ) combined
            WHERE DATE(submission_time) BETWEEN ? AND ?
            GROUP BY DATE(submission_time)
            ORDER BY date
        """;
        
        List<Map<String, Object>> trends = jdbcTemplate.queryForList(sql, startDate, endDate);
        
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            final LocalDate date = currentDate;
            Optional<Map<String, Object>> dayStats = trends.stream()
                .filter(m -> date.equals(m.get("date")))
                .findFirst();
            
            if (dayStats.isPresent()) {
                Map<String, Object> stats = dayStats.get();
                long submissions = convertToLong(stats.get("submissions"));
                long correctSubmissions = convertToLong(stats.get("correct_submissions"));
                double correctRate = submissions > 0 ? (double) correctSubmissions / submissions : 0;
                
                result.add(Map.of(
                    "date", date.toString(),
                    "submissions", submissions,
                        "correctRate", correctRate));
            } else {
                result.add(Map.of(
                    "date", date.toString(),
                        "submissions", 0L,
                        "correctRate", 0.0));
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        return Map.of("data", result);
    }
}