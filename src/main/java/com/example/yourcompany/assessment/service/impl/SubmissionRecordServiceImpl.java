package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.dto.CodeSubmitRequestDTO;
import com.example.yourcompany.assessment.dto.JudgeRequest;
import com.example.yourcompany.assessment.dto.JudgeResult;
import com.example.yourcompany.assessment.dto.SubmissionRecordDTO;
import com.example.yourcompany.assessment.entity.*;

import com.example.yourcompany.assessment.repository.AlgorithmQuestionRepository;
import com.example.yourcompany.assessment.repository.SubmissionRecordRepository;
import com.example.yourcompany.assessment.repository.TestCaseRepository;
import com.example.yourcompany.assessment.repository.UserRepository;
import com.example.yourcompany.assessment.service.SubmissionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:19
 **/
@Service
@RequiredArgsConstructor
public class SubmissionRecordServiceImpl implements SubmissionRecordService {

    private final TestCaseRepository testCaseRepository;
    private final AlgorithmQuestionRepository algorithmQuestionRepository;
    private final UserRepository userRepository;
    private final SubmissionRecordRepository submissionRecordRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    // 语言映射表
    private static final Map<String, Integer> LANGUAGE_MAP = Map.of(
            "cpp", 54,    // C++ (GCC 9.2.0)
            "java", 62,   // Java (OpenJDK 13.0.1)
            "python", 71, // Python (3.8.1)
            "javascript", 63  // JavaScript (Node.js 12.14.0)
    );
//    @Value("${judge0.api.url}")
    private String judge0ApiUrl="http://192.168.254.137:2358";  // 配置文件中设置: judge0.api.url=http://192.168.254.137:2358
    @Transactional
    @Override
    public SubmissionRecordDTO createSubmissionRecord(CodeSubmitRequestDTO  submissionDTO) {
        System.out.println("开始处理提交："+submissionDTO);

        // 1. 获取题目和用户
        AlgorithmQuestion question = algorithmQuestionRepository.findById(submissionDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("题目不存在"));

        User user = userRepository.findById(submissionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 获取题目的所有测试用例
        List<TestCase> testCases = testCaseRepository.findByQuestionQuestionId(submissionDTO.getQuestionId());
        if (testCases.isEmpty()) {
            throw new RuntimeException("题目没有测试用例");
        }
        System.out.println("testCases:"+testCases);
        // 3. 获取对应的judge0语言ID
        Integer languageId = LANGUAGE_MAP.get(submissionDTO.getLanguage().toLowerCase());
        if (languageId == null) {
            throw new RuntimeException("不支持的编程语言: " + submissionDTO.getLanguage());
        }
        System.out.println("languageId:"+languageId);
        // 4. 执行判题
        List<JudgeResult> results = new ArrayList<>();
        int passedCount = 0;

        for (TestCase testCase : testCases) {
            JudgeRequest judgeRequest = new JudgeRequest();
            judgeRequest.setSourceCode(submissionDTO.getCode());
            judgeRequest.setLanguageId(languageId);
            judgeRequest.setStdin(testCase.getInputData());
            judgeRequest.setExpectedOutput(testCase.getOutputData());

            try {
                JudgeResult result = submitToJudge0(judgeRequest);
                results.add(result);

                if (result.getStatus().contains("Accepted")) {
                    passedCount++;
                }
            } catch (Exception e) {
                System.out.println("判题过程出错"+e);
                throw new RuntimeException("判题服务异常", e);
            }
        }
        System.out.println("判题完成");
        // 5. 计算最终结果并创建提交记录
// 计算通过的百分比
        double passPercentage = (double) passedCount / testCases.size() * 100;

// 格式化为两位小数
        String finalStatus = String.format("%.2f%%", passPercentage);

        // 获取最大执行时间和内存使用
        double maxTime = results.stream()
                .mapToDouble(JudgeResult::getTime)
                .max()
                .orElse(0.0);

        double maxMemory = results.stream()
                .mapToDouble(JudgeResult::getMemory)
                .max()
                .orElse(0.0);

        // 6. 构建并保存提交记录
        SubmissionRecord submission = SubmissionRecord.builder()
                .question(question)
                .user(user)
                .sourceCode(submissionDTO.getCode())
                .language(submissionDTO.getLanguage())
                .status(finalStatus)
                .executionTime(String.format("%.2f ms", maxTime))
                .memoryConsumption(String.format("%.2f MB", maxMemory / 1024)) // 转换为MB
                .build();

        SubmissionRecord savedSubmission = submissionRecordRepository.save(submission);

        // 7. 转换为DTO并返回
        return convertToDTO(savedSubmission);
    }

    @Override
    public List<SubmissionRecordDTO> createBatchSubmissionRecord(List<CodeSubmitRequestDTO> requests) {
        List<SubmissionRecordDTO> results = new ArrayList<>();
        for (CodeSubmitRequestDTO request : requests) {
            SubmissionRecordDTO result = createSubmissionRecord(request);
            results.add(result);
        }

        return results;
    }


    private SubmissionRecordDTO convertToDTO(SubmissionRecord submission) {
        return SubmissionRecordDTO.builder()
                .id(submission.getId())
                .submissionTime(submission.getSubmissionTime())
                .executionTime(submission.getExecutionTime())
                .memoryConsumption(submission.getMemoryConsumption())
                .status(submission.getStatus())
                .language(submission.getLanguage())
                .questionId(submission.getQuestion().getQuestionId())
                .userId(submission.getUser().getUserId())
                .sourceCode(submission.getSourceCode())  // 添加这一行
                .build();
    }

    private JudgeResult submitToJudge0(JudgeRequest request) {
        String url = judge0ApiUrl + "/submissions?wait=true";

        Map<String, Object> body = Map.of(
                "source_code", request.getSourceCode(),
                "language_id", request.getLanguageId(),
                "stdin", request.getStdin(),
                "expected_output", request.getExpectedOutput()
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Judge0 服务响应错误: " + response.getStatusCode());
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Judge0 返回空响应");
        }

        JudgeResult result = new JudgeResult();
        result.setStatus(String.valueOf(responseBody.get("status")));
        result.setStdout(String.valueOf(responseBody.get("stdout")));
        result.setStderr(String.valueOf(responseBody.get("stderr")));
        result.setTime(Double.valueOf(String.valueOf(responseBody.get("time"))));
        result.setMemory(Double.valueOf(String.valueOf(responseBody.get("memory"))));

        return result;
    }



    @Override
    public SubmissionRecordDTO getSubmissionRecordById(Integer id) {
        return null;
    }

    @Override
    public List<SubmissionRecordDTO> getAllSubmissionRecords() {
        return null;
    }

    @Override
    public List<SubmissionRecordDTO> getSubmissionRecordsByUserId(Integer userId) {
        return null;
    }

    @Override
    public List<SubmissionRecordDTO> getSubmissionRecordsByQuestionId(Integer questionId) {
        List<SubmissionRecord> submissions = submissionRecordRepository
                .findByQuestionQuestionId(questionId);
//        System.out.println(submissions);
        return submissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionRecordDTO> getSubmissionRecordsByQuestionIdAndUserId(Integer userId, Integer questionId) {
        List<SubmissionRecord> submissions = submissionRecordRepository
                .findByUserUserIdAndQuestionQuestionId(userId, questionId);

        return submissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestCaseResult> getTestCaseResults(Integer submissionId) {
        return null;
    }
}
