package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.repository.*;
import com.example.yourcompany.assessment.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminStatisticsServiceImpl implements AdminStatisticsService {
    private final UserRepository userRepository;
    private final KnowledgeQuestionRepository knowledgeQuestionRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;
    private final PracticeRecordRepository practiceRecordRepository;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final SubmissionRecordRepository submissionRecordRepository;
    private final TestRepository testRepository;
    private final TestRecordRepository testRecordRepository;
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
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
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
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }
        return 0.0;
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);
        LocalDate monthAgo = today.minusMonths(1);

        // 计算总用户数
        long totalUsers = userRepository.count();

        // 获取近一周新增用户数
        String newUsersSql = "SELECT COUNT(*) FROM users WHERE created_at >= ?";
        long newLastWeek = convertToLong(jdbcTemplate.queryForObject(newUsersSql, Long.class, weekAgo));

        // 获取活跃用户数
        String activeUsersSql = """
                    SELECT
                        COUNT(DISTINCT user_id) as count
                    FROM (
                        SELECT user_id FROM practice_records WHERE submitted_at >= ?
                        UNION
                        SELECT user_id FROM submission_records WHERE submission_time >= ?
                        UNION
                        SELECT user_id FROM test_record WHERE start_time >= ?
                    ) as active_users
                """;

        // 日活跃
        long activeDaily = convertToLong(jdbcTemplate.queryForObject(
                activeUsersSql, Long.class, today, today, today));

        // 周活跃
        long activeWeekly = convertToLong(jdbcTemplate.queryForObject(
                activeUsersSql, Long.class, weekAgo, weekAgo, weekAgo));

        // 月活跃
        long activeMonthly = convertToLong(jdbcTemplate.queryForObject(
                activeUsersSql, Long.class, monthAgo, monthAgo, monthAgo));

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", totalUsers);
        result.put("newLastWeek", newLastWeek);
        result.put("activeDaily", activeDaily);
        result.put("activeWeekly", activeWeekly);
        result.put("activeMonthly", activeMonthly);

        return result;
    }

    @Override
    public Map<String, Object> getQuestionStatistics() {
        // 获取知识题和算法题总数
        long knowledgeTotal = knowledgeQuestionRepository.count();
        long algorithmTotal = algorithmQuestionRepository.count();

        // 获取题目难度分布
        String difficultySql = """
                    SELECT
                        'knowledge' as type,
                        difficulty,
                        COUNT(*) as count
                    FROM knowledge_questions
                    GROUP BY difficulty
                    UNION ALL
                    SELECT
                        'algorithm' as type,
                        difficulty,
                        COUNT(*) as count
                    FROM algorithm_questions
                    GROUP BY difficulty
                """;

        List<Map<String, Object>> difficultyStats = jdbcTemplate.queryForList(difficultySql);

        Map<String, Long> byDifficulty = new HashMap<>();
        byDifficulty.put("easy", 0L);
        byDifficulty.put("medium", 0L);
        byDifficulty.put("hard", 0L);

        for (Map<String, Object> stat : difficultyStats) {
            String difficulty = (String) stat.get("difficulty");
            long count = convertToLong(stat.get("count"));
            byDifficulty.put(difficulty, byDifficulty.getOrDefault(difficulty, 0L) + count);
        }

        // 获取章节分布
        String chapterSql = """
                    SELECT
                        'knowledge' as type,
                        chapter,
                        COUNT(*) as count
                    FROM knowledge_questions
                    GROUP BY chapter
                    UNION ALL
                    SELECT
                        'algorithm' as type,
                        chapter,
                        COUNT(*) as count
                    FROM algorithm_questions
                    GROUP BY chapter
                """;

        List<Map<String, Object>> chapterStats = jdbcTemplate.queryForList(chapterSql);
        Map<String, Map<String, Long>> byChapter = new HashMap<>();

        for (Map<String, Object> stat : chapterStats) {
            String type = (String) stat.get("type");
            String chapter = (String) stat.get("chapter");
            long count = convertToLong(stat.get("count"));

            byChapter.putIfAbsent(chapter, new HashMap<>());
            Map<String, Long> chapterData = byChapter.get(chapter);
            chapterData.put(type, count);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("knowledgeTotal", knowledgeTotal);
        result.put("algorithmTotal", algorithmTotal);
        result.put("byDifficulty", byDifficulty);
        result.put("byChapter", byChapter);

        return result;
    }

    @Override
    public Map<String, Object> getSubmissionStatistics() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);

        // 计算总提交数
        long submissionTotal = submissionRecordRepository.count();
        long total = submissionTotal;

        // 获取近一周提交数
        String recentSql = """
                    SELECT COUNT(*) FROM submission_records
                    WHERE submission_time >= ?
                """;

        long lastWeek = convertToLong(jdbcTemplate.queryForObject(recentSql, Long.class, weekAgo));

        // 计算提交成功率
        String successRateSql = """
                    SELECT
                        COUNT(CASE WHEN status = '100.00%' THEN 1 END) as successful,
                        COUNT(*) as total
                    FROM submission_records
                """;

        Map<String, Object> rateStats = jdbcTemplate.queryForMap(successRateSql);
        long successful = convertToLong(rateStats.get("successful"));
        long totalSubmissions = convertToLong(rateStats.get("total"));
        double successRate = totalSubmissions > 0 ? (double) successful / totalSubmissions : 0;

        // 获取提交趋势
        String trendSql = """
                    SELECT
                        date(submission_time) as date,
                        COUNT(*) as count,
                        SUM(CASE WHEN status = '100.00%' THEN 1 ELSE 0 END) as successful
                    FROM submission_records
                    WHERE submission_time >= ?
                    GROUP BY date(submission_time)
                    ORDER BY date
                """;

        List<Map<String, Object>> trendData = jdbcTemplate.queryForList(trendSql, weekAgo.minusDays(1));

        // 获取编程语言分布
        String languageSql = """
                    SELECT
                        language,
                        COUNT(*) as count
                    FROM submission_records
                    GROUP BY language
                """;

        List<Map<String, Object>> languageStats = jdbcTemplate.queryForList(languageSql);
        Map<String, Long> byLanguage = new HashMap<>();

        for (Map<String, Object> stat : languageStats) {
            String language = (String) stat.get("language");
            long count = convertToLong(stat.get("count"));
            byLanguage.put(language, count);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("lastWeek", lastWeek);
        result.put("successRate", successRate);
        result.put("trend", trendData);
        result.put("byLanguage", byLanguage);

        return result;
    }

    @Override
    public Map<String, Object> getTestStatistics() {
        // 获取考试总数
        long total = testRepository.count();

        // 获取平均分数和完成率
        String testStatsSql = """
                    SELECT
                        AVG(correct_count * 100.0 / total_questions) as avg_score,
                        COUNT(*) as completed,
                        (SELECT COUNT(*) FROM test_record) as total
                    FROM test_record
                    WHERE status = 'COMPLETED'
                """;

        Map<String, Object> statsResult = jdbcTemplate.queryForMap(testStatsSql);
        double avgScore = convertToDouble(statsResult.get("avg_score"));
        long completed = convertToLong(statsResult.get("completed"));
        long totalRecords = convertToLong(statsResult.get("total"));
        double completionRate = totalRecords > 0 ? (double) completed / totalRecords : 0;

        // 按难度获取考试统计
        String byDifficultySql = """
                    SELECT
                        CASE
                            WHEN easy_questions_num > medium_questions_num AND easy_questions_num > hard_questions_num THEN 'easy'
                            WHEN medium_questions_num > easy_questions_num AND medium_questions_num > hard_questions_num THEN 'medium'
                            ELSE 'hard'
                        END as difficulty,
                        COUNT(t.test_id) as total,
                        COUNT(tr.record_id) as taken,
                        COUNT(CASE WHEN tr.status = 'COMPLETED' THEN 1 ELSE NULL END) as completed,
                        AVG(CASE WHEN tr.status = 'COMPLETED' THEN tr.correct_count * 100.0 / tr.total_questions ELSE NULL END) as avg_score
                    FROM tests t
                    LEFT JOIN test_record tr ON t.test_id = tr.test_id
                    GROUP BY difficulty
                """;

        List<Map<String, Object>> difficultyStats = jdbcTemplate.queryForList(byDifficultySql);
        Map<String, Map<String, Object>> byDifficulty = new HashMap<>();

        for (Map<String, Object> stat : difficultyStats) {
            String difficulty = (String) stat.get("difficulty");
            long testCount = convertToLong(stat.get("total"));
            long completedCount = convertToLong(stat.get("completed"));
            double diffAvgScore = convertToDouble(stat.get("avg_score"));

            Map<String, Object> diffData = new HashMap<>();
            diffData.put("total", testCount);
            diffData.put("completed", completedCount);
            diffData.put("avgScore", diffAvgScore);

            byDifficulty.put(difficulty, diffData);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("avgScore", avgScore);
        result.put("completionRate", completionRate);
        result.put("byDifficulty", byDifficulty);

        return result;
    }

    @Override
    public List<Map<String, Object>> getChapterDetailedStatistics() {
        // 获取知识和算法章节的详细统计信息
        String chapterDetailSql = """
                    SELECT
                        c.chapter as name,
                        SUM(CASE WHEN q.type = 'knowledge' THEN 1 ELSE 0 END) as knowledge_count,
                        SUM(CASE WHEN q.type = 'algorithm' THEN 1 ELSE 0 END) as algorithm_count,
                        SUM(CASE WHEN q.difficulty = 'easy' THEN 1 ELSE 0 END) as easy_count,
                        SUM(CASE WHEN q.difficulty = 'medium' THEN 1 ELSE 0 END) as medium_count,
                        SUM(CASE WHEN q.difficulty = 'hard' THEN 1 ELSE 0 END) as hard_count,
                        COUNT(*) as total_count,
                        SUM(q.attempts) as total_attempts,
                        SUM(q.correct) as total_correct
                    FROM (
                        SELECT
                            chapter,
                            'knowledge' as type,
                            difficulty,
                            COUNT(pr.record_id) as attempts,
                            SUM(CASE WHEN pr.is_correct THEN 1 ELSE 0 END) as correct
                        FROM knowledge_questions kq
                        LEFT JOIN practice_records pr ON kq.question_id = pr.question_id
                        GROUP BY kq.question_id, kq.chapter, kq.difficulty
                        UNION ALL
                        SELECT
                            chapter,
                            'algorithm' as type,
                            difficulty,
                            COUNT(sr.id) as attempts,
                            SUM(CASE WHEN sr.status = '100.00%' THEN 1 ELSE 0 END) as correct
                        FROM algorithm_questions aq
                        LEFT JOIN submission_records sr ON aq.question_id = sr.question_id
                        GROUP BY aq.question_id, aq.chapter, aq.difficulty
                    ) as q
                    RIGHT JOIN (
                        SELECT chapter FROM chapter_knowledge
                        UNION
                        SELECT chapter FROM chapter_algorithm
                    ) as c ON q.chapter = c.chapter
                    GROUP BY c.chapter
                    ORDER BY c.chapter
                """;

        List<Map<String, Object>> chapterDetails = jdbcTemplate.queryForList(chapterDetailSql);

        // 为每一个章节计算正确率
        for (Map<String, Object> chapter : chapterDetails) {
            long totalAttempts = convertToLong(chapter.get("total_attempts"));
            long totalCorrect = convertToLong(chapter.get("total_correct"));
            double correctRate = totalAttempts > 0 ? (double) totalCorrect / totalAttempts : 0;
            chapter.put("correctRate", correctRate);
        }

        return chapterDetails;
    }

    @Override
    public List<Map<String, Object>> getUserDetailedStatistics() {
        LocalDate today = LocalDate.now();
        // 获取过去12周的用户详细统计数据
        List<Map<String, Object>> userDetails = new ArrayList<>();

        // 获取每周的新增用户、活跃用户和提交用户
        for (int i = 11; i >= 0; i--) {
            LocalDate weekStart = today.minusWeeks(i);
            LocalDate weekEnd = weekStart.plusDays(6);

            String periodLabel = weekStart.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                    " 到 " + weekEnd.format(DateTimeFormatter.ISO_LOCAL_DATE);

            // 新增用户
            String newUsersSql = "SELECT COUNT(*) FROM users WHERE created_at BETWEEN ? AND ?";
            long newUsers = convertToLong(jdbcTemplate.queryForObject(
                    newUsersSql, Long.class, weekStart, weekEnd.plusDays(1)));

            // 活跃用户
            String activeUsersSql = """
                        SELECT COUNT(DISTINCT user_id) FROM (
                            SELECT user_id FROM practice_records WHERE submitted_at BETWEEN ? AND ?
                            UNION
                            SELECT user_id FROM submission_records WHERE submission_time BETWEEN ? AND ?
                            UNION
                            SELECT user_id FROM test_record WHERE start_time BETWEEN ? AND ?
                        ) as active_users
                    """;

            LocalDateTime weekStartTime = weekStart.atStartOfDay();
            LocalDateTime weekEndTime = weekEnd.plusDays(1).atStartOfDay();

            long activeUsers = convertToLong(jdbcTemplate.queryForObject(
                    activeUsersSql, Long.class,
                    weekStartTime, weekEndTime,
                    weekStartTime, weekEndTime,
                    weekStartTime, weekEndTime));

            // 提交用户
            String submittingUsersSql = """
                        SELECT COUNT(DISTINCT user_id) FROM (
                            SELECT user_id FROM submission_records WHERE submission_time BETWEEN ? AND ?
                        ) as submitting_users
                    """;

            long submittingUsers = convertToLong(jdbcTemplate.queryForObject(
                    submittingUsersSql, Long.class, weekStartTime, weekEndTime));

            Map<String, Object> weekData = new HashMap<>();
            weekData.put("period", periodLabel);
            weekData.put("newUsers", newUsers);
            weekData.put("activeUsers", activeUsers);
            weekData.put("submittingUsers", submittingUsers);

            userDetails.add(weekData);
        }

        return userDetails;
    }

    @Override
    public List<Map<String, Object>> getSubmissionDetailedStatistics() {
        LocalDate today = LocalDate.now();
        List<Map<String, Object>> submissionDetails = new ArrayList<>();

        // 获取过去30天的提交数据
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            String submissionsSql = """
                        SELECT
                            COUNT(*) as total,
                            SUM(CASE WHEN status = '100.00%' THEN 1 ELSE 0 END) as successful
                        FROM submission_records
                        WHERE submission_time BETWEEN ? AND ?
                    """;

            Map<String, Object> submissionStat = jdbcTemplate.queryForMap(
                    submissionsSql, startOfDay, endOfDay);

            long totalSubmissions = convertToLong(submissionStat.get("total"));
            long successfulSubmissions = convertToLong(submissionStat.get("successful"));
            long failedSubmissions = totalSubmissions - successfulSubmissions;
            double successRate = totalSubmissions > 0 ? (double) successfulSubmissions / totalSubmissions : 0;

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            dayData.put("totalSubmissions", totalSubmissions);
            dayData.put("successfulSubmissions", successfulSubmissions);
            dayData.put("failedSubmissions", failedSubmissions);
            dayData.put("successRate", successRate);

            submissionDetails.add(dayData);
        }

        return submissionDetails;
    }

    @Override
    public List<Map<String, Object>> getTestDetailedStatistics() {
        // 获取所有考试的详细统计数据
        String testDetailSql = """
                    SELECT
                        t.test_id as id,
                        t.title,
                        t.chapter,
                        CASE
                            WHEN t.easy_questions_num > t.medium_questions_num AND t.easy_questions_num > t.hard_questions_num THEN 'easy'
                            WHEN t.medium_questions_num > t.easy_questions_num AND t.medium_questions_num > t.hard_questions_num THEN 'medium'
                            ELSE 'hard'
                        END as difficulty,
                        COUNT(tr.record_id) as participant_count,
                        AVG(CASE WHEN tr.status = 'COMPLETED' THEN tr.correct_count * 100.0 / tr.total_questions ELSE NULL END) as avg_score,
                        MAX(CASE WHEN tr.status = 'COMPLETED' THEN tr.correct_count * 100.0 / tr.total_questions ELSE 0 END) as highest_score
                    FROM tests t
                    LEFT JOIN test_record tr ON t.test_id = tr.test_id
                    GROUP BY t.test_id, t.title, t.chapter, difficulty
                    ORDER BY t.created_at DESC
                """;

        List<Map<String, Object>> testDetails = jdbcTemplate.queryForList(testDetailSql);

        return testDetails;
    }
}