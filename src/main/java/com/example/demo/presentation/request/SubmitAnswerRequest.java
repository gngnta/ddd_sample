package com.example.demo.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "SubmitAnswerRequest")
public class SubmitAnswerRequest {
    private Integer questionId;
    private Integer choiceId;
}
