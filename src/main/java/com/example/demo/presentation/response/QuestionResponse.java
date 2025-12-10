package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "QuestionResponse")
public class QuestionResponse {
    private Integer id;
    private Integer categoryId;
    private String questionText;
    private List<QuestionChoiceResponse> choices;
}
