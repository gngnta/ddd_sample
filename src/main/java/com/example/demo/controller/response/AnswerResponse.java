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
public class AnswerResponse {
    private Integer id;
    private Integer attemptId;
    private Integer questionId;
    private Integer choiceId;
    private Boolean isCorrect;
    private OffsetDateTime answeredAt;
}
