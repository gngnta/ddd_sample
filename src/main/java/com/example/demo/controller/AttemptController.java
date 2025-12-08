package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.controller.response.AttemptResultResponse;
import com.example.demo.controller.response.AttemptSummaryResponse;
import com.example.demo.controller.response.NextQuestionResponse;
import com.example.demo.service.AttemptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/start")
    public ResponseEntity<AttemptStartResponse> startAttempt(@RequestBody StartAttemptRequest request) {
        AttemptStartResponse response = attemptService.startAttempt(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{attemptId}/question")
    public ResponseEntity<NextQuestionResponse> getNextQuestion(@PathVariable("attemptId") Long attemptId) {
        return attemptService.getNextQuestion(attemptId);
    }

    @PostMapping("/{attemptId}/answer")
    public ResponseEntity<AnswerResponse> submitAnswer(
            @PathVariable("attemptId") Long attemptId,
            @RequestBody SubmitAnswerRequest request) {
        AnswerResponse response = attemptService.submitAnswer(attemptId, request);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{attemptId}/result")
    public ResponseEntity<AttemptResultResponse> getAttemptResult(@PathVariable("attemptId") Long attemptId) {
        AttemptResultResponse response = attemptService.getResult(attemptId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<AttemptSummaryResponse> listAttemptSummaries() {
        return attemptService.listAttemptSummaries();
    }

    @GetMapping("/{attemptId}/answers")
    public List<AnswerDetailResponse> listAnswerDetails(@PathVariable("attemptId") Long attemptId) {
        return attemptService.getAnswerDetails(attemptId);
    }
}
