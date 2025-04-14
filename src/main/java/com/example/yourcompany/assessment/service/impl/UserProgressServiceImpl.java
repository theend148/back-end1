package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.entity.Test1;
import com.example.yourcompany.assessment.entity.TestRecord;
import com.example.yourcompany.assessment.entity.User;
import com.example.yourcompany.assessment.repository.*;
import com.example.yourcompany.assessment.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户学习进度服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserProgressServiceImpl implements UserProgressService {
    private final UserRepository userRepository;
    private final KnowledgeQuestionRepository knowledgeQuestionRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;
    private final PracticeRecordRepository practiceRecordRepository;
    private final SubmissionRecordRepository submissionRecordRepository;
    private final TestRepository testRepository;
    private final TestRecordRepository testRecordRepository;
    private final KnowledgeChapterRepository knowledgeChapterRepository;
    private final AlgorithmChapterRepository algorithmChapterRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getUserProgress(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        // 确保用户存在
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("用户不存在: " + userId);
        }

        // 1. 计算总体概览数据
        result.put("overview", calculateOverview(userId));

        // 2. 计算知识题进度
        result.put("knowledgeProgress", calculateKnowledgeProgress(userId));

        // 3. 计算算法题进度
        result.put("algorithmProgress", calculateAlgorithmProgress(userId));

        // 4. 计算章节进度
        result.put("chapterProgress", calculateChapterProgress(userId));

        // 5. 计算考试表现
        result.put("testPerformance", calculateTestPerformance(userId));

        // 6. 计算近期提交情况
        result.put("recentSubmissions", calculateRecentSubmissions(userId));

        // 7. 计算时间分布
        result.put("timeDistribution", calculateTimeDistribution(userId));

        // 8. 计算难度分布
        result.put("difficultyDistribution", calculateDifficultyDistribution(userId));

        return result;
    }

    /**
     * 计算总体概览数据
     */
    private Map<String, Object> calculateOverview(Integer userId) {
        Map<String, Object> overview = new HashMap<>();

        // 总题目数（知识题 + 算法题）
        long totalKnowledgeQuestions = knowledgeQuestionRepository.count();
        long totalAlgorithmQuestions = algorithmQuestionRepository.count();
        long totalQuestions = totalKnowledgeQuestions + totalAlgorithmQuestions;
        overview.put("totalQuestions", totalQuestions);

        // 已尝试的题目数
        String attemptedSql = """
                SELECT
                    COUNT(DISTINCT CASE WHEN pr.question_type = 'knowledge' THEN pr.question_id END) +
                    COUNT(DISTINCT CASE WHEN sr.id IS NOT NULL THEN sr.question_id END) as attempted_count
                FROM
                    (SELECT ? as user_id) u
                LEFT JOIN
                    practice_records pr ON u.user_id = pr.user_id
                LEFT JOIN
                    submission_records sr ON u.user_id = sr.user_id
                """;
        Long attemptedQuestions = jdbcTemplate.queryForObject(attemptedSql, Long.class, userId);
        overview.put("attemptedQuestions", attemptedQuestions != null ? attemptedQuestions : 0);

        // 正确题目数
        String correctSql = """
                SELECT
                    COUNT(DISTINCT CASE WHEN pr.question_type = 'knowledge' AND pr.is_correct = 1 THEN pr.question_id END) +
                    COUNT(DISTINCT CASE WHEN sr.status = '100.00%' THEN sr.question_id END) as correct_count
                FROM
                    (SELECT ? as user_id) u
                LEFT JOIN
                    practice_records pr ON u.user_id = pr.user_id
                LEFT JOIN
                    submission_records sr ON u.user_id = sr.user_id
                """;
        Long correctQuestions = jdbcTemplate.queryForObject(correctSql, Long.class, userId);
        overview.put("correctQuestions", correctQuestions != null ? correctQuestions : 0);

        // 提交率（已尝试题目数 / 总题目数）
        double submissionRate = totalQuestions > 0
                ? (double) attemptedQuestions / totalQuestions
                : 0;
        overview.put("submissionRate", submissionRate);

        return overview;
    }

    /**
     * 计算知识题进度
     */
    private Map<String, Object> calculateKnowledgeProgress(Integer userId) {
        Map<String, Object> knowledgeProgress = new HashMap<>();

        // 总知识题数
        long total = knowledgeQuestionRepository.count();
        knowledgeProgress.put("total", total);

        // 已尝试的知识题数
        String attemptedSql = """
                SELECT COUNT(DISTINCT question_id)
                FROM practice_records
                WHERE user_id = ? AND question_type = 'knowledge'
                """;
        Long attempted = jdbcTemplate.queryForObject(attemptedSql, Long.class, userId);
        knowledgeProgress.put("attempted", attempted != null ? attempted : 0);

        // 正确的知识题数
        String correctSql = """
                SELECT COUNT(DISTINCT question_id)
                FROM practice_records
                WHERE user_id = ? AND question_type = 'knowledge' AND is_correct = 1
                """;
        Long correct = jdbcTemplate.queryForObject(correctSql, Long.class, userId);
        knowledgeProgress.put("correct", correct != null ? correct : 0);

        // 正确率
        double correctRate = attempted != null && attempted > 0
                ? (double) correct / attempted
                : 0;
        knowledgeProgress.put("correctRate", correctRate);

        return knowledgeProgress;
    }

    /**
     * 计算算法题进度
     */
    private Map<String, Object> calculateAlgorithmProgress(Integer userId) {
        Map<String, Object> algorithmProgress = new HashMap<>();

        // 总算法题数
        long total = algorithmQuestionRepository.count();
        algorithmProgress.put("total", total);

        // 已尝试的算法题数
        String attemptedSql = """
                SELECT COUNT(DISTINCT question_id)
                FROM submission_records
                WHERE user_id = ?
                """;
        Long attempted = jdbcTemplate.queryForObject(attemptedSql, Long.class, userId);
        algorithmProgress.put("attempted", attempted != null ? attempted : 0);

        // 成功提交的数量
        String submissionsSql = """
                SELECT COUNT(DISTINCT question_id)
                FROM submission_records
                WHERE user_id = ? AND status = '100.00%'
                """;
        Long submissions = jdbcTemplate.queryForObject(submissionsSql, Long.class, userId);
        algorithmProgress.put("submissions", submissions != null ? submissions : 0);

        // 成功率
        double successRate = attempted != null && attempted > 0
                ? (double) submissions / attempted
                : 0;
        algorithmProgress.put("successRate", successRate);

        return algorithmProgress;
    }

    /**
     * 计算章节进度
     */
    private List<Map<String, Object>> calculateChapterProgress(Integer userId) {
        List<Map<String, Object>> chapterProgress = new ArrayList<>();

        // 获取所有知识点章节
        List<String> knowledgeChapters = knowledgeChapterRepository.findAll().stream()
                .map(chapter -> chapter.getChapter())
                .collect(Collectors.toList());

        // 获取所有算法章节
        List<String> algorithmChapters = algorithmChapterRepository.findAll().stream()
                .map(chapter -> chapter.getChapter())
                .collect(Collectors.toList());

        // 合并所有章节
        Set<String> allChapters = new HashSet<>();
        allChapters.addAll(knowledgeChapters);
        allChapters.addAll(algorithmChapters);

        for (String chapter : allChapters) {
            Map<String, Object> chapterData = new HashMap<>();
            chapterData.put("chapter", chapter);

            // 计算知识题完成率和正确率
            if (knowledgeChapters.contains(chapter)) {
                String knowledgeSql = """
                        SELECT
                            COUNT(DISTINCT q.question_id) as total,
                            COUNT(DISTINCT CASE WHEN pr.record_id IS NOT NULL THEN q.question_id END) as attempted,
                            COUNT(DISTINCT CASE WHEN pr.is_correct = 1 THEN q.question_id END) as correct
                        FROM
                            knowledge_questions q
                        LEFT JOIN
                            practice_records pr ON q.question_id = pr.question_id AND pr.user_id = ?
                        WHERE
                            q.chapter = ?
                        """;
                Map<String, Object> knowledgeStats = jdbcTemplate.queryForMap(knowledgeSql, userId, chapter);

                long kTotal = ((Number) knowledgeStats.get("total")).longValue();
                long kAttempted = ((Number) knowledgeStats.get("attempted")).longValue();
                long kCorrect = ((Number) knowledgeStats.get("correct")).longValue();

                double kCompletionRate = kTotal > 0 ? (double) kAttempted / kTotal : 0;
                double kCorrectRate = kAttempted > 0 ? (double) kCorrect / kAttempted : 0;

                chapterData.put("knowledgeTotal", kTotal);
                chapterData.put("knowledgeAttempted", kAttempted);
                chapterData.put("knowledgeCorrect", kCorrect);
                chapterData.put("knowledgeCompletionRate", kCompletionRate);
                chapterData.put("knowledgeCorrectRate", kCorrectRate);
            }

            // 计算算法题完成率和正确率
            if (algorithmChapters.contains(chapter)) {
                String algorithmSql = """
                        SELECT
                            COUNT(DISTINCT q.question_id) as total,
                            COUNT(DISTINCT CASE WHEN sr.id IS NOT NULL THEN q.question_id END) as attempted,
                            COUNT(DISTINCT CASE WHEN sr.status = '100.00%' THEN q.question_id END) as correct
                        FROM
                            algorithm_questions q
                        LEFT JOIN
                            submission_records sr ON q.question_id = sr.question_id AND sr.user_id = ?
                        WHERE
                            q.chapter = ?
                        """;
                Map<String, Object> algorithmStats = jdbcTemplate.queryForMap(algorithmSql, userId, chapter);

                long aTotal = ((Number) algorithmStats.get("total")).longValue();
                long aAttempted = ((Number) algorithmStats.get("attempted")).longValue();
                long aCorrect = ((Number) algorithmStats.get("correct")).longValue();

                double aCompletionRate = aTotal > 0 ? (double) aAttempted / aTotal : 0;
                double aCorrectRate = aAttempted > 0 ? (double) aCorrect / aAttempted : 0;

                chapterData.put("algorithmTotal", aTotal);
                chapterData.put("algorithmAttempted", aAttempted);
                chapterData.put("algorithmCorrect", aCorrect);
                chapterData.put("algorithmCompletionRate", aCompletionRate);
                chapterData.put("algorithmCorrectRate", aCorrectRate);
            }

            // 计算总体完成率和正确率
            long total = (chapterData.containsKey("knowledgeTotal") ? (long) chapterData.get("knowledgeTotal") : 0) +
                    (chapterData.containsKey("algorithmTotal") ? (long) chapterData.get("algorithmTotal") : 0);

            long attempted = (chapterData.containsKey("knowledgeAttempted")
                    ? (long) chapterData.get("knowledgeAttempted")
                    : 0) +
                    (chapterData.containsKey("algorithmAttempted") ? (long) chapterData.get("algorithmAttempted") : 0);

            long correct = (chapterData.containsKey("knowledgeCorrect") ? (long) chapterData.get("knowledgeCorrect")
                    : 0) +
                    (chapterData.containsKey("algorithmCorrect") ? (long) chapterData.get("algorithmCorrect") : 0);

            double completionRate = total > 0 ? (double) attempted / total : 0;
            double correctRate = attempted > 0 ? (double) correct / attempted : 0;

            chapterData.put("total", total);
            chapterData.put("attempted", attempted);
            chapterData.put("correct", correct);
            chapterData.put("completionRate", completionRate);
            chapterData.put("correctRate", correctRate);

            chapterProgress.add(chapterData);
        }

        return chapterProgress;
    }

    /**
     * 计算考试表现
     */
    private Map<String, Object> calculateTestPerformance(Integer userId) {
        Map<String, Object> testPerformance = new HashMap<>();

        // 获取用户的所有考试记录
        List<TestRecord> testRecords = testRecordRepository.findByUserId(userId);
        int totalTests = testRecords.size();

        // 平均正确率和最高正确率
        double avgScore = 0;
        double bestScore = 0;

        if (!testRecords.isEmpty()) {
            // 计算所有考试的平均正确率，并防止除以零
            avgScore = testRecords.stream()
                    .filter(record -> record.getTotalQuestions() != null && record.getTotalQuestions() > 0)
                    .mapToDouble(record -> {
                        try {
                            return (double) record.getCorrectCount() / record.getTotalQuestions() * 100;
                        } catch (Exception e) {
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0);

            // 计算所有考试的最高正确率，并防止除以零
            bestScore = testRecords.stream()
                    .filter(record -> record.getTotalQuestions() != null && record.getTotalQuestions() > 0)
                    .mapToDouble(record -> {
                        try {
                            return (double) record.getCorrectCount() / record.getTotalQuestions() * 100;
                        } catch (Exception e) {
                            return 0.0;
                        }
                    })
                    .max()
                    .orElse(0);
        }

        // 处理NaN和Infinity情况
        if (Double.isNaN(avgScore) || Double.isInfinite(avgScore)) {
            avgScore = 0.0;
        }
        if (Double.isNaN(bestScore) || Double.isInfinite(bestScore)) {
            bestScore = 0.0;
        }

        // 按照考试ID分组，找出每个考试中正确率最高的记录和用时最短的记录
        Map<Integer, List<TestRecord>> testRecordsByTestId = testRecords.stream()
                .collect(Collectors.groupingBy(TestRecord::getTestId));

        // 存储最佳记录（每个考试只保留一条）
        List<TestRecord> bestRecords = new ArrayList<>();

        for (List<TestRecord> records : testRecordsByTestId.values()) {
            if (records.isEmpty()) {
                continue;
            }

            // 优先按正确率排序，其次是用时
            TestRecord bestRecord = records.stream()
                    .sorted((r1, r2) -> {
                        try {
                            // 首先比较正确率（高的优先）
                            double correctRate1 = r1.getTotalQuestions() > 0
                                    ? (double) r1.getCorrectCount() / r1.getTotalQuestions()
                                    : 0;
                            double correctRate2 = r2.getTotalQuestions() > 0
                                    ? (double) r2.getCorrectCount() / r2.getTotalQuestions()
                                    : 0;

                            // 处理NaN情况
                            if (Double.isNaN(correctRate1))
                                correctRate1 = 0;
                            if (Double.isNaN(correctRate2))
                                correctRate2 = 0;

                            int rateComparison = Double.compare(correctRate2, correctRate1);
                            if (rateComparison != 0) {
                                return rateComparison;
                            }

                            // 正确率相同，比较用时（短的优先）
                            Long time1 = r1.getTotalTime() != null ? r1.getTotalTime() : Long.MAX_VALUE;
                            Long time2 = r2.getTotalTime() != null ? r2.getTotalTime() : Long.MAX_VALUE;

                            return Long.compare(time1, time2);
                        } catch (Exception e) {
                            return 0; // 如果比较出错，视为相等
                        }
                    })
                    .findFirst()
                    .orElse(null);

            if (bestRecord != null) {
                bestRecords.add(bestRecord);
            }
        }

        // 获取最近的考试记录（按开始时间倒序，只取最佳记录）
        List<Map<String, Object>> recentTests = bestRecords.stream()
                .sorted(Comparator.comparing(TestRecord::getStartTime).reversed())
                .map(record -> {
                    Map<String, Object> testData = new HashMap<>();

                    try {
                        // 获取考试信息
                        Test1 test = testRepository.getByTestId(record.getTestId());
                        if (test != null) {
                            testData.put("title", test.getTitle());
                            testData.put("chapter", test.getChapter());
                        } else {
                            testData.put("title", "未知考试");
                            testData.put("chapter", "未知章节");
                        }

                        // 计算分数，避免除以零
                        double score = 0.0;
                        if (record.getTotalQuestions() != null && record.getTotalQuestions() > 0) {
                            score = (double) record.getCorrectCount() / record.getTotalQuestions() * 100;
                            // 处理NaN情况
                            if (Double.isNaN(score) || Double.isInfinite(score)) {
                                score = 0.0;
                            }
                        }
                        testData.put("score", score);

                        // 获取考试用时（秒）
                        long durationInSeconds = record.getTotalTime() != null ? record.getTotalTime() : 0;

                        // 格式化为 "小时:分钟:秒"
                        long hours = durationInSeconds / 3600;
                        long minutes = (durationInSeconds % 3600) / 60;
                        long seconds = durationInSeconds % 60;
                        String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                        testData.put("duration", duration);

                        // 计算完成时间（开始时间 + 持续时间）
                        LocalDateTime completedDateTime = null;
                        if (record.getStartTime() != null && record.getTotalTime() != null) {
                            completedDateTime = record.getStartTime().plusSeconds(record.getTotalTime());
                        } else {
                            completedDateTime = record.getStartTime(); // 如果没有持续时间，则使用开始时间
                        }

                        // 格式化完成时间
                        String completedAt = completedDateTime != null
                                ? completedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                : "";
                        testData.put("completedAt", completedAt);
                    } catch (Exception e) {
                        // 处理任何计算中的异常，确保至少有基本数据
                        testData.putIfAbsent("title", "未知考试");
                        testData.putIfAbsent("chapter", "未知章节");
                        testData.putIfAbsent("score", 0.0);
                        testData.putIfAbsent("duration", "00:00:00");
                        testData.putIfAbsent("completedAt", "");
                    }

                    return testData;
                })
                .collect(Collectors.toList());

        // 更新总考试数为考试ID的数量（去重后）
        int uniqueTestCount = testRecordsByTestId.size();
        testPerformance.put("totalTests", uniqueTestCount);
        testPerformance.put("avgScore", avgScore);
        testPerformance.put("bestScore", bestScore);
        testPerformance.put("recentTests", recentTests);
        System.out.println("testPerformance: " + testPerformance);

        return testPerformance;
    }

    /**
     * 计算近期提交情况
     */
    private List<Map<String, Object>> calculateRecentSubmissions(Integer userId) {
        // 获取过去30天的每日提交情况
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(29); // 获取30天的数据（包括今天）

        // 准备日期列表（从30天前到今天）
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dateList.add(thirtyDaysAgo.plusDays(i));
        }

        // 查询用户在这段时间内的每日提交和正确率
        String submissionTrendSql = """
                SELECT
                    DATE(time) as date,
                    COUNT(*) as submissions,
                    SUM(CASE WHEN is_successful THEN 1 ELSE 0 END) / COUNT(*) as correct_rate
                FROM (
                    SELECT
                        submitted_at as time,
                        is_correct as is_successful
                    FROM
                        practice_records
                    WHERE
                        user_id = ? AND submitted_at >= ?
                    UNION ALL
                    SELECT
                        submission_time as time,
                        CASE WHEN status = '100.00%' THEN true ELSE false END as is_successful
                    FROM
                        submission_records
                    WHERE
                        user_id = ? AND submission_time >= ?
                ) as submissions
                GROUP BY
                    DATE(time)
                ORDER BY
                    date
                """;

        // 查询每日提交数据
        List<Map<String, Object>> submissionData = jdbcTemplate.queryForList(
                submissionTrendSql,
                userId,
                thirtyDaysAgo.atStartOfDay(),
                userId,
                thirtyDaysAgo.atStartOfDay());

        // 将查询结果转换为日期映射
        Map<String, Map<String, Object>> dateToSubmissionMap = new HashMap<>();
        for (Map<String, Object> data : submissionData) {
            String dateStr = ((java.sql.Date) data.get("date")).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            dateToSubmissionMap.put(dateStr, data);
        }

        // 构建最终结果（包括没有提交的日期）
        List<Map<String, Object>> recentSubmissions = new ArrayList<>();
        for (LocalDate date : dateList) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);

            if (dateToSubmissionMap.containsKey(dateStr)) {
                Map<String, Object> data = dateToSubmissionMap.get(dateStr);
                dayData.put("submissions", ((Number) data.get("submissions")).longValue());
                dayData.put("correctRate", ((Number) data.get("correct_rate")).doubleValue());
            } else {
                dayData.put("submissions", 0);
                dayData.put("correctRate", 0.0);
            }

            recentSubmissions.add(dayData);
        }

        return recentSubmissions;
    }

    /**
     * 计算时间分布
     */
    private Map<String, Object> calculateTimeDistribution(Integer userId) {
        Map<String, Object> timeDistribution = new HashMap<>();

        // 计算知识题平均用时
        String knowledgeTimeSql = """
                SELECT AVG(TIMESTAMPDIFF(SECOND, submitted_at, submitted_at)) as avg_time
                FROM practice_records
                WHERE user_id = ? AND question_type = 'knowledge'
                """;
        Double avgTimeKnowledge = jdbcTemplate.queryForObject(knowledgeTimeSql, Double.class, userId);
        timeDistribution.put("avgTimeKnowledge", avgTimeKnowledge != null ? avgTimeKnowledge.intValue() : 0);

        // 计算算法题平均用时（暂不考虑，因为submission_records表中可能没有用时信息）
        // 这里使用默认值
        timeDistribution.put("avgTimeAlgorithm", 360); // 默认为6分钟

        // 按难度级别的平均用时
        Map<String, Object> timeByDifficulty = new HashMap<>();
        // 这里使用默认值，实际项目中应根据实际情况计算
        timeByDifficulty.put("easy", 180); // 默认简单题3分钟
        timeByDifficulty.put("medium", 420); // 默认中等题7分钟
        timeByDifficulty.put("hard", 720); // 默认困难题12分钟

        timeDistribution.put("timeByDifficulty", timeByDifficulty);

        return timeDistribution;
    }

    /**
     * 计算难度分布
     */
    private Map<String, Object> calculateDifficultyDistribution(Integer userId) {
        Map<String, Object> difficultyDistribution = new HashMap<>();

        // 按难度分类的知识题统计
        String knowledgeSql = """
                SELECT
                    q.difficulty,
                    COUNT(DISTINCT q.question_id) as total,
                    COUNT(DISTINCT CASE WHEN pr.record_id IS NOT NULL THEN q.question_id END) as attempted,
                    COUNT(DISTINCT CASE WHEN pr.is_correct = 1 THEN q.question_id END) as correct
                FROM
                    knowledge_questions q
                LEFT JOIN
                    practice_records pr ON q.question_id = pr.question_id AND pr.user_id = ?
                GROUP BY
                    q.difficulty
                """;
        List<Map<String, Object>> knowledgeStats = jdbcTemplate.queryForList(knowledgeSql, userId);

        // 按难度分类的算法题统计
        String algorithmSql = """
                SELECT
                    q.difficulty,
                    COUNT(DISTINCT q.question_id) as total,
                    COUNT(DISTINCT CASE WHEN sr.id IS NOT NULL THEN q.question_id END) as attempted,
                    COUNT(DISTINCT CASE WHEN sr.status = '100.00%' THEN q.question_id END) as correct
                FROM
                    algorithm_questions q
                LEFT JOIN
                    submission_records sr ON q.question_id = sr.question_id AND sr.user_id = ?
                GROUP BY
                    q.difficulty
                """;
        List<Map<String, Object>> algorithmStats = jdbcTemplate.queryForList(algorithmSql, userId);

        // 合并统计结果
        Map<String, Map<String, Long>> difficultyMap = new HashMap<>();
        difficultyMap.put("easy", new HashMap<>());
        difficultyMap.put("medium", new HashMap<>());
        difficultyMap.put("hard", new HashMap<>());

        // 初始化难度数据
        for (String difficulty : Arrays.asList("easy", "medium", "hard")) {
            Map<String, Long> stats = difficultyMap.get(difficulty);
            stats.put("total", 0L);
            stats.put("attempted", 0L);
            stats.put("correct", 0L);
        }

        // 合并知识题统计
        for (Map<String, Object> stat : knowledgeStats) {
            String difficulty = String.valueOf(stat.get("difficulty")).toLowerCase();
            if (difficultyMap.containsKey(difficulty)) {
                Map<String, Long> stats = difficultyMap.get(difficulty);
                stats.put("total", stats.get("total") + ((Number) stat.get("total")).longValue());
                stats.put("attempted", stats.get("attempted") + ((Number) stat.get("attempted")).longValue());
                stats.put("correct", stats.get("correct") + ((Number) stat.get("correct")).longValue());
            }
        }

        // 合并算法题统计
        for (Map<String, Object> stat : algorithmStats) {
            String difficulty = String.valueOf(stat.get("difficulty")).toLowerCase();
            if (difficultyMap.containsKey(difficulty)) {
                Map<String, Long> stats = difficultyMap.get(difficulty);
                stats.put("total", stats.get("total") + ((Number) stat.get("total")).longValue());
                stats.put("attempted", stats.get("attempted") + ((Number) stat.get("attempted")).longValue());
                stats.put("correct", stats.get("correct") + ((Number) stat.get("correct")).longValue());
            }
        }

        // 构建返回结果
        for (String difficulty : Arrays.asList("easy", "medium", "hard")) {
            Map<String, Long> stats = difficultyMap.get(difficulty);
            Map<String, Object> difficultyData = new HashMap<>();

            difficultyData.put("total", stats.get("total"));
            difficultyData.put("attempted", stats.get("attempted"));

            double correctRate = stats.get("attempted") > 0
                    ? (double) stats.get("correct") / stats.get("attempted")
                    : 0;
            difficultyData.put("correctRate", correctRate);

            difficultyDistribution.put(difficulty, difficultyData);
        }

        return difficultyDistribution;
    }

    /**
     * 安全转换为Long类型
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            return 0L;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}