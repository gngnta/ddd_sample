package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuestionChoiceResponse")
public class QuestionChoiceResponse {
    private Integer id;
    private Integer questionId;
    private String choiceText;
}
