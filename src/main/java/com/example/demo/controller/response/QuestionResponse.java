package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private Integer id;
    private Integer categoryId;
    private String questionText;
    private List<QuestionChoiceResponse> choices;
}
