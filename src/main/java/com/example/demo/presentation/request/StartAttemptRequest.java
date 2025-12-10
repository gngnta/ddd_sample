package com.example.demo.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "StartAttemptRequest")
public class StartAttemptRequest {
    private Integer categoryId;
}
