package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.request.StartAttemptRequest;
import com.example.demo.controller.request.SubmitAnswerRequest;
import com.example.demo.controller.response.AnswerDetailResponse;
import com.example.demo.controller.response.AnswerResponse;
import com.example.demo.controller.response.AttemptStartResponse;
import com.example.demo.controller.response.AttemptSummaryResponse;
import com.example.demo.controller.response.NextQuestionResponse;
import com.example.demo.service.AttemptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attempts")
@Tag(name = "Attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping
    @Operation(summary = "Start a new attempt")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Attempt started")
    })
    public ResponseEntity<AttemptStartResponse> startAttempt(@RequestBody StartAttemptRequest request) {
        AttemptStartResponse response = attemptService.startAttempt(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{attempt_id}/question")
    @Operation(summary = "Get next question")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Next question"),
    })
    public ResponseEntity<NextQuestionResponse> getNextQuestion(@PathVariable("attempt_id") Integer attemptId) {
        NextQuestionResponse response = attemptService.getNextQuestion(attemptId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{attempt_id}/answer")
    @Operation(summary = "Submit answer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Answer stored")
    })
    public ResponseEntity<AnswerResponse> submitAnswer(
            @PathVariable("attempt_id") Integer attemptId,
            @RequestBody SubmitAnswerRequest request) {
        AnswerResponse response = attemptService.submitAnswer(attemptId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all attempts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attempt list")
    })
    public List<AttemptSummaryResponse> listAttemptSummaries() {
        return attemptService.listAttemptSummaries();
    }

    @GetMapping("/{attempt_id}")
    @Operation(summary = "Get attempt by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attempt details")
    })
    public ResponseEntity<AttemptSummaryResponse> getAttempt(@PathVariable("attempt_id") Integer attemptId) {
        AttemptSummaryResponse response = attemptService.getAttempt(attemptId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attempt_id}/answers")
    @Operation(summary = "Get answer history")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Answer history")
    })
    public List<AnswerDetailResponse> listAnswerDetails(@PathVariable("attempt_id") Integer attemptId) {
        return attemptService.getAnswerDetails(attemptId);
    }
}
