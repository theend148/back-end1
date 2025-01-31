package com.example.yourcompany.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qianyue
 * @Date 2025.01.17 21:33
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeSubmitRequestDTO {
    Integer userId;
    Integer questionId;
    String language;
    String code;
}
