package com.example.demo.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AttemptResultResponse")
public class AttemptResultResponse {
    private Integer attemptId;
    private Integer categoryId;
    private String categoryName;
    private Integer totalQuestions;
    private Integer correctCount;
    private OffsetDateTime completedAt;
}
