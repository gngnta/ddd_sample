package com.example.demo.controller.request;

import lombok.Data;

@Data
public class SubmitAnswerRequest {
    private Integer questionId;
    private Integer choiceId;
}
