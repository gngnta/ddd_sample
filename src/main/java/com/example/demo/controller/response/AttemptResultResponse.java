package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptResultResponse {
    private Integer attemptId;
    private Integer categoryId;
    private String categoryName;
    private Integer totalQuestions;
    private Integer correctCount;
    private OffsetDateTime completedAt;
}
