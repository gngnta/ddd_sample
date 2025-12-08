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
public class AnswerDetailResponse {
    private Integer answerId;
    private Integer questionId;
    private String questionText;
    private Integer choiceId;
    private String choiceText;
    private Boolean isCorrect;
    private OffsetDateTime answeredAt;
}
