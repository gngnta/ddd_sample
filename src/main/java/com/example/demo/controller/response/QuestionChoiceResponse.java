package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionChoiceResponse {
    private Integer id;
    private Integer questionId;
    private String choiceText;
}
