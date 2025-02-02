package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.dto.*;
import com.example.yourcompany.assessment.entity.*;
import com.example.yourcompany.assessment.repository.*;
import com.example.yourcompany.assessment.service.SubmissionRecordService;
import com.example.yourcompany.assessment.service.TestService;
//import jakarta.persistence.EntityNotFoundException;
import com.example.yourcompany.assessment.util.QuestionResult;
import com.example.yourcompany.assessment.util.QuestionResult1;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final UserRepository userRepository;

    private final TestRepository testRepository;
    private final SubmissionRecordRepository submissionRecordRepository;

    private final SubmissionRecordService submissionRecordService;


    private final KnowledgeQuestionRepository knowledgeQuestionRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;

    private  final TestRecordRepository testRecordRepository;
    private  final TestAnswerDetailRepository testAnswerDetailRepository;



    private final TestQuestionRepository testQuestionRepository;


    @Override
    public double calculateUserTestAverage(Integer userId) {
        return 0;
    }

    @Override
    public double calculateChapterTestAverage(String chapter) {
        // 实现计算特定章节的平均分数逻辑
        return 0.0;
    }



    @Override
    public GetTestResponse getTestById(Integer testId) {
        Optional<Test1> optionalTest1 = testRepository.findById(testId);
        if (optionalTest1.isPresent()) {
            Test1 test11 = optionalTest1.get();
            List<Integer> questionsIds=new ArrayList<>();
            List<TestQuestions>tmp=testQuestionRepository.getByTestId(test11.getTestId());
            for(TestQuestions questions:tmp){
                questionsIds.add(questions.getQuestionId());
            }
            GetTestResponse ans=new GetTestResponse(test11,questionsIds,test11.getGenerateType());
            return ans;
            // 进一步处理 test1
        } else {
            throw new RuntimeException("Test1 not found with id: " + testId);
        }
    }

    //    TODO 待优化
    @Override
    public List<GetTestResponse> getAllTests() {
        List<Test1> test1=testRepository.findAll();
        List<GetTestResponse>ans=new ArrayList<>();

        for(Test1 test11 : test1){
            List<Integer> questionsIds=new ArrayList<>();
            List<TestQuestions>tmp=testQuestionRepository.getByTestId(test11.getTestId());
            for(TestQuestions questions:tmp){
                questionsIds.add(questions.getQuestionId());
            }
            ans.add(new GetTestResponse(test11,questionsIds,test11.getGenerateType()));
        }

        return ans;
    }

    @Override
    public Test1DTO updateTest(Integer testId,Test1DTO testDTO) {
        Test1 obj=convertToTest(testDTO);
        obj.setTestId(testId);
        return convertToTest1DTO(testRepository.save(obj));
    }

    private float calculateScore(int correctAnswers, int totalQuestions) {
        return totalQuestions == 0 ? 0 : (float) correctAnswers / totalQuestions * 100;
    }

    private Test1DTO convertToTest1DTO(Test1 dto){
        Test1DTO obj = new Test1DTO();
        obj.setTestId(dto.getTestId());
        obj.setTitle(dto.getTitle());
        obj.setDescription(dto.getDescription());
        obj.setDuration(dto.getDuration());
        obj.setChapter(dto.getChapter());
        obj.setTotalQuestions(dto.getTotalQuestions());
        obj.setCreatedAt(dto.getCreatedAt());
        obj.setQuestionType(dto.getQuestionType());
        obj.setEasyQuestionsNum(dto.getEasyQuestionsNum());
        obj.setMediumQuestionsNum(dto.getMediumQuestionsNum());
        obj.setHardQuestionsNum(dto.getHardQuestionsNum());
        obj.setGenerateType(dto.getGenerateType());
        return obj;
    }

    private Test1 convertToTest(Test1DTO dto) {
        Test1 obj = new Test1();
//        obj.setTestId(dto.getTestId());
        obj.setTitle(dto.getTitle());
        obj.setDescription(dto.getDescription());
        obj.setDuration(dto.getDuration());
        obj.setChapter(dto.getChapter());
        obj.setTotalQuestions(dto.getTotalQuestions());
        obj.setCreatedAt(dto.getCreatedAt());
        obj.setQuestionType(dto.getQuestionType());
        obj.setEasyQuestionsNum(dto.getEasyQuestionsNum());
        obj.setMediumQuestionsNum(dto.getMediumQuestionsNum());
        obj.setHardQuestionsNum(dto.getHardQuestionsNum());
        obj.setGenerateType(dto.getGenerateType());
        return obj;
    }

    //-----
    @Override
    public Test1DTO createTest(CreateTestRequest request) {
        Test1 test = new Test1();
        test.setTitle(request.getTitle());
        test.setDescription(request.getDescription());
        test.setDuration(request.getDuration());
        test.setChapter(request.getChapter());
        test.setQuestionType(request.getQuestionType());
        test.setEasyQuestionsNum(request.getEasyQuestionsNum());
        test.setMediumQuestionsNum(request.getMediumQuestionsNum());
        test.setHardQuestionsNum(request.getHardQuestionsNum());
        test.setTotalQuestions(request.getEasyQuestionsNum() +
                request.getMediumQuestionsNum() +
                request.getHardQuestionsNum());
        test.setGenerateType(request.getIsAutoGenerate());
        Test1 test1=testRepository.save(test);
        System.out.println(test1);

        Test1DTO ans=convertToTest1DTO(test1);

        if (request.getIsAutoGenerate()&&request.getQuestionType() == QuestionCategory.knowledge) {
            autoSelectKnowledgeQuestions(
                    request.getChapter(),
                    request.getEasyQuestionsNum(),
                    request.getMediumQuestionsNum(),
                    request.getHardQuestionsNum(), test1.getTestId()
            );
        }
        if (request.getIsAutoGenerate()&&request.getQuestionType() == QuestionCategory.algorithm) {
            autoSelectAlgorithmQuestions(
                    request.getChapter(),
                    request.getEasyQuestionsNum(),
                    request.getMediumQuestionsNum(),
                    request.getHardQuestionsNum(), test1.getTestId()
            );
        }
        if (!request.getIsAutoGenerate()&&request.getQuestionType() == QuestionCategory.knowledge) {
            notAutoSelectKnowledgeQuestions(
                    request.getChapter(),request.getQuestionIds(), test1.getTestId());
        }
        if (!request.getIsAutoGenerate()&&request.getQuestionType() == QuestionCategory.algorithm) {
            notAutoSelectAlgorithmQuestions( request.getChapter(),request.getQuestionIds(), test1.getTestId());
        }
        return ans;


    }
//

    private void autoSelectKnowledgeQuestions(String chapter,
                                               int easyCount, int mediumCount, int hardCount,Integer testId) {
        List<KnowledgeQuestion> questions =knowledgeQuestionRepository.findByChapter(chapter);

        Map<String, List<KnowledgeQuestion>> groupedByDifficulty = questions.stream()
                .collect(Collectors.groupingBy((q -> q.getDifficulty().name()))); // 按难度分类

        // 获取每个难度的题目 id 列表
        List<Integer> easyIds = groupedByDifficulty.getOrDefault("easy", new ArrayList<>()).stream()
                .map(KnowledgeQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> mediumIds = groupedByDifficulty.getOrDefault("medium", new ArrayList<>()).stream()
                .map(KnowledgeQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> hardIds = groupedByDifficulty.getOrDefault("hard", new ArrayList<>()).stream()
                .map(KnowledgeQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> selectedIds = new ArrayList<>();

        // 随机选择 easy ID
        selectedIds.addAll(selectRandomIds(easyIds, easyCount));

        // 随机选择 medium ID
        selectedIds.addAll(selectRandomIds(mediumIds, mediumCount));

        // 随机选择 hard ID
        selectedIds.addAll(selectRandomIds(hardIds, hardCount));

        for(int i=0;i<selectedIds.size();i++){
            TestQuestions obj=new TestQuestions();
            obj.setTestId(testId);
            obj.setQuestionId(selectedIds.get(i));
            obj.setQuestionType(QuestionCategory.knowledge);
            testQuestionRepository.save(obj);
        }


    }

    private List<Integer> selectRandomIds(List<Integer> ids, int count) {
        // 如果题目 ID 数量少于请求的数量，返回所有题目 ID
        if (ids.size() <= count) {
            return new ArrayList<>(ids);
        }
        // 打乱题目 ID 列表
        Collections.shuffle(ids);
        // 返回随机选择的前 count 个题目 ID
        return ids.subList(0, count);
    }


    private void autoSelectAlgorithmQuestions(String chapter,
                                              int easyCount, int mediumCount, int hardCount,Integer testId) {
        List<AlgorithmQuestion> questions =algorithmQuestionRepository.findByChapter(chapter);

        Map<String, List<AlgorithmQuestion>> groupedByDifficulty = questions.stream()
                .collect(Collectors.groupingBy((q -> q.getDifficulty().name()))); // 按难度分类

        // 获取每个难度的题目 id 列表
        List<Integer> easyIds = groupedByDifficulty.getOrDefault("easy", new ArrayList<>()).stream()
                .map(AlgorithmQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> mediumIds = groupedByDifficulty.getOrDefault("medium", new ArrayList<>()).stream()
                .map(AlgorithmQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> hardIds = groupedByDifficulty.getOrDefault("hard", new ArrayList<>()).stream()
                .map(AlgorithmQuestion::getQuestionId)
                .collect(Collectors.toList());

        List<Integer> selectedIds = new ArrayList<>();

        // 随机选择 easy ID
        selectedIds.addAll(selectRandomIds(easyIds, easyCount));

        // 随机选择 medium ID
        selectedIds.addAll(selectRandomIds(mediumIds, mediumCount));

        // 随机选择 hard ID
        selectedIds.addAll(selectRandomIds(hardIds, hardCount));

        for(int i=0;i<selectedIds.size();i++){
            TestQuestions obj=new TestQuestions();
            obj.setTestId(testId);
            obj.setQuestionId(selectedIds.get(i));
            obj.setQuestionType(QuestionCategory.algorithm);
            testQuestionRepository.save(obj);
        }


    }
    @Override
    public TestResultDTO submitTest(Integer testId, TestSubmitDTO testSubmit){
        Integer totalCorrect=0,easyCorrect=0,mediumCorrect=0,hardCorrect=0,easyNum=0,mediumNum=0,hardNum=0;
        Test1 test=testRepository.getByTestId(testId);
        QuestionCategory type=test.getQuestionType();
        List<QuestionResult> questionResults = new ArrayList<>();
        TestRecord testRecord = new TestRecord(0,testId,testSubmit.getUserId(),LocalDateTime.now(),0,0,0,0,0,0,0,0,0,"");
        testRecordRepository.save(testRecord);
        List<TestRecord> testRecords= testRecordRepository.getByTestId(testId);
        Integer recordId=0;
        if (testRecords != null && !testRecords.isEmpty()) {
            TestRecord lastTestRecord = testRecords.get(testRecords.size() - 1);
            recordId = lastTestRecord.getRecordId();
            // 这里可以使用 recordId 继续处理
        } else {
            // 处理没有找到记录的情况
            System.out.println("");
        }
        Boolean flag=false;



        if(type==QuestionCategory.knowledge){
            for (Map.Entry<Integer, String> entry : testSubmit.getAnswers().entrySet()) {
                Integer questionId = entry.getKey();
                String userAnswer = entry.getValue();

                KnowledgeQuestion question = knowledgeQuestionRepository.getByQuestionId(questionId);


                if (question.getDifficulty() == Difficulty.easy) {
                    easyNum++;
                    if (userAnswer.equals(question.getCorrectAnswer())) {
                        easyCorrect++;
                        totalCorrect++;
                        flag=true;
                    }
                } else if (question.getDifficulty() == Difficulty.medium) {
                    mediumNum++;
                    if (userAnswer.equals(question.getCorrectAnswer())) {
                        mediumCorrect++;
                        totalCorrect++;
                        flag=true;

                    }
                } else {
                    hardNum++;
                    if (userAnswer.equals(question.getCorrectAnswer())) {
                        hardCorrect++;
                        totalCorrect++;
                        flag=true;
                    }
                }
                TestAnswerDetail testAnswerDetail = new TestAnswerDetail(recordId,questionId,userAnswer,flag);
                flag = false;
                testAnswerDetailRepository.save(testAnswerDetail);


                // Add question result to list
                QuestionResult result = new QuestionResult();
                result.setQuestionId(questionId);
                result.setIsCorrect(userAnswer.equals(question.getCorrectAnswer()));
                result.setCorrectAnswer(question.getCorrectAnswer());
                questionResults.add(result);
            }

            // Calculate accuracies
            double overallAccuracy = totalCorrect / (double) testSubmit.getAnswers().size();
            double easyAccuracy = easyNum > 0 ? easyCorrect / (double) easyNum : 0.0;
            double mediumAccuracy = mediumNum > 0 ? mediumCorrect / (double) mediumNum : 0.0;
            double hardAccuracy = hardNum > 0 ? hardCorrect / (double) hardNum : 0.0;

            Map<String, Double> accuracyByDifficulty = new HashMap<>();
            accuracyByDifficulty.put("easy", easyAccuracy);
            accuracyByDifficulty.put("medium", mediumAccuracy);
            accuracyByDifficulty.put("hard", hardAccuracy);

            // Populate TestResultDTO
            TestResultDTO resultDTO = new TestResultDTO();
            resultDTO.setTestId(testId);
            resultDTO.setUserId(testSubmit.getUserId());
            resultDTO.setOverallAccuracy(overallAccuracy);
            resultDTO.setAccuracyByDifficulty(accuracyByDifficulty);
            resultDTO.setQuestionResults(questionResults);

            testRecord = new TestRecord(recordId,testId,testSubmit.getUserId(),testSubmit.getStartTime(),
                    testSubmit.getTotalTime(),testSubmit.getAnswers().size(),totalCorrect,easyNum,easyCorrect,mediumNum,mediumCorrect,hardNum,hardCorrect,"");
            testRecordRepository.save(testRecord);

            return resultDTO;
        }
        else{
//
        }
        return null;

    }

    @Override
    public TestResult1DTO submitTest1(Integer testId, TestSubmit1DTO testSubmit) {
        Integer totalCorrect = 0, easyCorrect = 0, mediumCorrect = 0, hardCorrect = 0;
        Integer easyNum = 0, mediumNum = 0, hardNum = 0;

        // 创建考试记录
        TestRecord testRecord = new TestRecord(0, testId, testSubmit.getUserId(),
                LocalDateTime.now(), 0, 0, 0, 0, 0, 0, 0, 0, 0, "");
        testRecordRepository.save(testRecord);

        // 获取记录ID
        List<TestRecord> testRecords = testRecordRepository.getByTestId(testId);
        //获取最新的记录
        Integer recordId = testRecords.get(testRecords.size() - 1).getRecordId();

        List<QuestionResult1> questionResults = new ArrayList<>();

        // 处理每道题的提交
        for (Map.Entry<Integer, String> entry : testSubmit.getAnswers().entrySet()) {
            Integer questionId = entry.getKey();
            String submissionId = entry.getValue();

            // 获取题目和提交记录
            AlgorithmQuestion question = algorithmQuestionRepository.getByQuestionId(questionId);
            SubmissionRecord submission = submissionRecordRepository.getById(Integer.parseInt(submissionId));

            // 判断是否正确（通过率100%为正确）
            boolean isCorrect = "100.00%".equals(submission.getStatus());

            // 统计不同难度的正确数
            if (question.getDifficulty() == Difficulty.easy) {
                easyNum++;
                if (isCorrect) {
                    easyCorrect++;
                    totalCorrect++;
                }
            } else if (question.getDifficulty() == Difficulty.medium) {
                mediumNum++;
                if (isCorrect) {
                    mediumCorrect++;
                    totalCorrect++;
                }
            } else {
                hardNum++;
                if (isCorrect) {
                    hardCorrect++;
                    totalCorrect++;
                }
            }

            // 保存答题记录
            TestAnswerDetail testAnswerDetail = new TestAnswerDetail(
                    recordId,
                    questionId,
                    submissionId,
                    isCorrect
            );
            testAnswerDetailRepository.save(testAnswerDetail);

            // 获取测试用例结果
            List<TestCaseResult> testCases = submissionRecordService.getTestCaseResults(Integer.parseInt(submissionId));

            // 构建题目结果
            QuestionResult1 result = QuestionResult1.builder()
                    .questionId(questionId)
                    .isCorrect(isCorrect)
                    .submissionId(submissionId)
                    .status(submission.getStatus())
                    .executionTime(submission.getExecutionTime())
                    .memoryUsage(submission.getMemoryConsumption())
                    .language(submission.getLanguage())
                    .code(submission.getSourceCode())
//                    .errorMessage(submission.getErrorMessage())
                    .testCases(testCases)
                    .build();

            questionResults.add(result);
        }

        // 计算正确率
        double overallAccuracy = totalCorrect / (double) testSubmit.getAnswers().size();
        double easyAccuracy = easyNum > 0 ? easyCorrect / (double) easyNum : 0.0;
        double mediumAccuracy = mediumNum > 0 ? mediumCorrect / (double) mediumNum : 0.0;
        double hardAccuracy = hardNum > 0 ? hardCorrect / (double) hardNum : 0.0;

        Map<String, Double> accuracyByDifficulty = new HashMap<>();
        accuracyByDifficulty.put("easy", easyAccuracy);
        accuracyByDifficulty.put("medium", mediumAccuracy);
        accuracyByDifficulty.put("hard", hardAccuracy);

        // 更新考试记录
        testRecord = new TestRecord(recordId, testId, testSubmit.getUserId(),
                testSubmit.getStartTime(), testSubmit.getTotalTime(),
                testSubmit.getAnswers().size(), totalCorrect,
                easyNum, easyCorrect, mediumNum, mediumCorrect,
                hardNum, hardCorrect, "");
        testRecordRepository.save(testRecord);

        // 构建返回结果
        return TestResult1DTO.builder()
                .testId(testId)
                .userId(testSubmit.getUserId())
                .overallAccuracy(overallAccuracy)
                .accuracyByDifficulty(accuracyByDifficulty)
                .questionResults(questionResults)
                .build();
    }

    @Override
    public TestResultDTO getTestResult(Integer testId) {
        return null;
    }

//    @Override
//    public TestResultDTO getTestResult(Integer testId) {
//        TestRecord obj=testRecordRepository.getByTestId(testId);
//        TestResultDTO resultDTO = new TestResultDTO();
//        resultDTO.setTestId(testId);
//        resultDTO.setUserId(obj.getUserId());
//        resultDTO.setOverallAccuracy();
//        resultDTO.setAccuracyByDifficulty(accuracyByDifficulty);
//        resultDTO.setQuestionResults(questionResults);
//    }

    private void notAutoSelectKnowledgeQuestions(String chapter, List<Integer>questionsId, Integer testId){
        for(int i=0;i<questionsId.size();i++){
            TestQuestions obj=new TestQuestions();
            obj.setTestId(testId);
            obj.setQuestionId(questionsId.get(i));
            obj.setQuestionType(QuestionCategory.knowledge);
            testQuestionRepository.save(obj);
        }
    }

    private void notAutoSelectAlgorithmQuestions(String chapter, List<Integer>questionsId, Integer testId){
        for(int i=0;i<questionsId.size();i++){
            TestQuestions obj=new TestQuestions();
            obj.setTestId(testId);
            obj.setQuestionId(questionsId.get(i));
            obj.setQuestionType(QuestionCategory.algorithm);
            testQuestionRepository.save(obj);
        }
    }



//    ---------testrecord
//    public TestRecordDTO submitTest(Integer testId, Integer userId, List<String> answers) {
//        // 获取试卷信息
//        Test1 test = this.getTestById(testId);
//        List<KnowledgeQuestion> questions = questionService.getQuestionsByIds(test.getQuestionIds());
//
//        // 创建考试记录
//        TestRecord record = new TestRecord();
//        record.setTest(test);
//        record.setUserId(userId);
//        record.setStartTime(LocalDateTime.now());
//        record.setTotalQuestions(questions.size());
//
//        // 统计各难度题目数量
//        Map<String, Integer> difficultyTotals = countDifficultyTotals(questions);
//        record.setEasyTotal(difficultyTotals.get("easy"));
//        record.setMediumTotal(difficultyTotals.get("medium"));
//        record.setHardTotal(difficultyTotals.get("hard"));
//
//        // 评分并记录答案
//        List<TestAnswerDetail> answerDetails = new ArrayList<>();
//        Map<String, Integer> correctCounts = new HashMap<>();
//
//        for (int i = 0; i < questions.size(); i++) {
//            KnowledgeQuestion question = questions.get(i);
//            String userAnswer = answers.get(i);
//            boolean isCorrect = checkAnswer(question, userAnswer);
//
//            // 记录答案
//            TestAnswerDetail detail = new TestAnswerDetail();
//            detail.setTestRecord(record);
//            detail.setQuestion(question);
//            detail.setUserAnswer(userAnswer);
//            detail.setIsCorrect(isCorrect);
//            answerDetails.add(detail);
//
//            // 统计正确数
//            if (isCorrect) {
//                String difficulty = question.getDifficulty();
//                correctCounts.merge(difficulty, 1, Integer::sum);
//            }
//        }
//
//        // 更新统计信息
//        record.setCorrectCount(correctCounts.values().stream().mapToInt(Integer::intValue).sum());
//        record.setEasyCorrect(correctCounts.getOrDefault("easy", 0));
//        record.setMediumCorrect(correctCounts.getOrDefault("medium", 0));
//        record.setHardCorrect(correctCounts.getOrDefault("hard", 0));
//        record.setStatus("completed");
//
//        // 保存记录
//        record = testRecordRepository.save(record);
//        answerDetailRepository.saveAll(answerDetails);
//
//        return convertToDTO(record);
//    }
//
//    public TestRecordDTO getTestResult(Integer testId, Integer userId) {
//        TestRecord record = testRecordRepository.findByTestIdAndUserId(testId, userId)
//                .orElseThrow(() -> new RuntimeException("考试记录不存在"));
//        return convertToDTO(record);
//    }
//
//    private boolean checkAnswer(KnowledgeQuestion question, String userAnswer) {
//        if (question.getQuestionType().equals("choice")) {
//            return question.getCorrectAnswer().equals(userAnswer);
//        } else {
//            return Boolean.parseBoolean(question.getCorrectAnswer()) == Boolean.parseBoolean(userAnswer);
//        }
//    }
//
//    private TestRecordDTO convertToDTO(TestRecord record) {
//        TestRecordDTO dto = new TestRecordDTO();
//        // ... 设置基本信息 ...
//
//        // 计算正确率
//        dto.setCorrectRate(calculateRate(record.getCorrectCount(), record.getTotalQuestions()));
//        dto.setEasyRate(calculateRate(record.getEasyCorrect(), record.getEasyTotal()));
//        dto.setMediumRate(calculateRate(record.getMediumCorrect(), record.getMediumTotal()));
//        dto.setHardRate(calculateRate(record.getHardCorrect(), record.getHardTotal()));
//
//        return dto;
//    }
//
//    private BigDecimal calculateRate(int correct, int total) {
//        if (total == 0) return BigDecimal.ZERO;
//        return BigDecimal.valueOf(correct)
//                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
//    }
}
