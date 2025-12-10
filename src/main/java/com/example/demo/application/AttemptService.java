package com.example.demo.application;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.application.mapper.AttemptMapper;
import com.example.demo.exception.NotFoundException;
import com.example.demo.infrastructure.AnswerRepository;
import com.example.demo.infrastructure.AttemptRepository;
import com.example.demo.infrastructure.CategoryRepository;
import com.example.demo.infrastructure.QuestionChoiceRepository;
import com.example.demo.infrastructure.QuestionRepository;
import com.example.demo.infrastructure.entity.AnswerEntity;
import com.example.demo.infrastructure.entity.AttemptEntity;
import com.example.demo.infrastructure.entity.CategoryEntity;
import com.example.demo.infrastructure.entity.QuestionChoiceEntity;
import com.example.demo.infrastructure.entity.QuestionEntity;
import com.example.demo.presentation.request.StartAttemptRequest;
import com.example.demo.presentation.request.SubmitAnswerRequest;
import com.example.demo.presentation.response.AnswerDetailResponse;
import com.example.demo.presentation.response.AnswerResponse;
import com.example.demo.presentation.response.AttemptStartResponse;
import com.example.demo.presentation.response.AttemptSummaryResponse;
import com.example.demo.presentation.response.NextQuestionResponse;
import com.example.demo.presentation.response.QuestionChoiceResponse;
import com.example.demo.presentation.response.QuestionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final AnswerRepository answerRepository;
    private final AttemptMapper attemptMapper;

    public AttemptStartResponse startAttempt(StartAttemptRequest request) {
        Integer totalQuestions = (int) questionRepository.countByCategoryId(request.getCategoryId());
        if (totalQuestions == 0) {
            throw new NotFoundException("No questions found for the specified category");
        }
        AttemptEntity attempt = AttemptEntity.builder()
                .categoryId(request.getCategoryId())
                .totalQuestions(totalQuestions)
                .correctCount(0)
                .createdAt(OffsetDateTime.now())
                .completedAt(null)
                .build();
        AttemptEntity saved = attemptRepository.save(attempt);
        return toAttemptStartResponse(saved);
    }

    public NextQuestionResponse getNextQuestion(Integer attemptId) {
        AttemptEntity attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found: " + attemptId));
        QuestionEntity question = questionRepository
                .findRandomQuestionExcludingAnswered(attemptId, attempt.getCategoryId())
                .orElseThrow(() -> new NotFoundException("No remaining questions for attempt: " + attemptId));
        List<QuestionChoiceResponse> choices = questionChoiceRepository.findByQuestionId(question.getId())
                .stream()
                .map(attemptMapper::toQuestionChoiceResponse)
                .toList();
        return NextQuestionResponse.builder()
                .attemptId(attemptId)
                .question(QuestionResponse.builder()
                        .id(question.getId())
                        .categoryId(question.getCategoryId())
                        .questionText(question.getQuestionText())
                        .choices(choices)
                        .build())
                .build();
    }

    public AnswerResponse submitAnswer(Integer attemptId, SubmitAnswerRequest request) {
        AttemptEntity attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found: " + attemptId));
        QuestionChoiceEntity choice = questionChoiceRepository.findById(request.getChoiceId())
                .orElseThrow(() -> new NotFoundException("Question choice not found: " + request.getChoiceId()));
        Boolean isCorrect = choice.getIsCorrect();
        AnswerEntity answer = AnswerEntity.builder()
                .attemptId(attemptId)
                .questionId(request.getQuestionId())
                .choiceId(request.getChoiceId())
                .answeredAt(OffsetDateTime.now())
                .build();
        AnswerEntity savedAnswer = answerRepository.save(answer);
        if (isCorrect) {
            attempt.setCorrectCount(attempt.getCorrectCount() + 1);
        }
        int answeredCount = (int) answerRepository.countByAttemptId(attemptId);
        if (attempt.getTotalQuestions() == answeredCount && attempt.getCompletedAt() == null) {
            attempt.setCompletedAt(OffsetDateTime.now());
        }
        attemptRepository.save(attempt);
        return AnswerResponse.builder()
                .id(savedAnswer.getId())
                .attemptId(attemptId)
                .questionId(request.getQuestionId())
                .choiceId(request.getChoiceId())
                .isCorrect(isCorrect)
                .answeredAt(savedAnswer.getAnsweredAt())
                .status(resolveStatus(attempt))
                .build();
    }

    public AttemptSummaryResponse getAttempt(Integer attemptId) {
        return attemptRepository.findById(attemptId)
                .map(this::toAttemptSummary)
                .orElseThrow(() -> new NotFoundException("Attempt not found: " + attemptId));
    }

    public List<AttemptSummaryResponse> listAttemptSummaries() {
        return attemptRepository.findAll().stream()
                .map(this::toAttemptSummary)
                .toList();
    }

    public List<AnswerDetailResponse> getAnswerDetails(Integer attemptId) {
        return answerRepository.findByAttemptId(attemptId).stream()
                .map(this::toAnswerDetail)
                .toList();
    }

    // Business logic methods
    private String resolveStatus(AttemptEntity attempt) {
        return attempt.getCompletedAt() != null ? "completed" : "in_progress";
    }

    private Boolean resolvePassed(AttemptEntity attempt) {
        if (attempt.getTotalQuestions() == null || attempt.getTotalQuestions() == 0) {
            return Boolean.FALSE;
        }
        int correct = Optional.ofNullable(attempt.getCorrectCount()).orElse(0);
        return correct * 2 >= attempt.getTotalQuestions();
    }

    private Long calculateDurationSeconds(AttemptEntity attempt) {
        if (attempt == null || attempt.getCreatedAt() == null || attempt.getCompletedAt() == null) {
            return null;
        }
        return Duration.between(attempt.getCreatedAt(), attempt.getCompletedAt()).getSeconds();
    }

    // Mapping methods
    private AttemptSummaryResponse toAttemptSummary(AttemptEntity attempt) {
        CategoryEntity category = categoryRepository.findById(attempt.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + attempt.getCategoryId()));
        return attemptMapper.toAttemptSummary(attempt, category, resolveStatus(attempt), resolvePassed(attempt),
                calculateDurationSeconds(attempt));
    }

    private AttemptStartResponse toAttemptStartResponse(AttemptEntity attempt) {
        return attemptMapper.toAttemptStart(attempt, resolveStatus(attempt), resolvePassed(attempt));
    }

    private AnswerDetailResponse toAnswerDetail(AnswerEntity answer) {
        QuestionEntity question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found: " + answer.getQuestionId()));
        QuestionChoiceEntity choice = questionChoiceRepository.findById(answer.getChoiceId())
                .orElseThrow(() -> new NotFoundException("Choice not found: " + answer.getChoiceId()));
        return attemptMapper.toAnswerDetail(answer, question, choice);
    }

}
