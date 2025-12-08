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
public class AttemptStartResponse {
    private Integer id;
    private Integer categoryId;
    private Integer totalQuestions;
    private Integer correctCount;
    private String status;
    private Boolean passed;
    private OffsetDateTime createdAt;
    private OffsetDateTime completedAt;
}
