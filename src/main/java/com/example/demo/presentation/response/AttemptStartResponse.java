package com.example.demo.controller.response;

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
@Schema(name = "AttemptStartResponse")
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
