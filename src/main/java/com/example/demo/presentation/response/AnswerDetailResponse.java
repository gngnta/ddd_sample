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
@Schema(name = "AnswerDetailResponse")
public class AnswerDetailResponse {
    private Integer answerId;
    private Integer questionId;
    private String questionText;
    private Integer choiceId;
    private String choiceText;
    private Boolean isCorrect;
    private OffsetDateTime answeredAt;
}
