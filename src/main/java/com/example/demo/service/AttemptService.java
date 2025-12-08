package com.example.demo.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.controller.request.StartAttemptRequest;
import com.example.demo.controller.request.SubmitAnswerRequest;
import com.example.demo.controller.response.AnswerDetailResponse;
import com.example.demo.controller.response.AnswerResponse;
import com.example.demo.controller.response.AttemptStartResponse;
import com.example.demo.controller.response.AttemptResultResponse;
import com.example.demo.controller.response.AttemptSummaryResponse;
import com.example.demo.controller.response.NextQuestionResponse;
import com.example.demo.controller.response.QuestionChoiceResponse;
import com.example.demo.controller.response.QuestionResponse;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.AttemptRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionChoiceRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.entity.AnswerEntity;
import com.example.demo.repository.entity.AttemptEntity;
import com.example.demo.repository.entity.CategoryEntity;
import com.example.demo.repository.entity.QuestionChoiceEntity;
import com.example.demo.repository.entity.QuestionEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final AnswerRepository answerRepository;

    public AttemptStartResponse startAttempt(StartAttemptRequest request) {
        List<QuestionEntity> questions = questionRepository.findByCategoryId(request.getCategoryId().longValue());
        AttemptEntity attempt = AttemptEntity.builder()
                .categoryId(request.getCategoryId().longValue())
                .totalQuestions(questions.size())
                .correctCount(0)
                .createdAt(OffsetDateTime.now())
                .completedAt(null)
                .build();
        AttemptEntity saved = attemptRepository.save(attempt);
        return toAttemptStartResponse(saved);
    }

    public ResponseEntity<NextQuestionResponse> getNextQuestion(Long attemptId) {
        AttemptEntity attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt == null) {
            return ResponseEntity.notFound().build();
        }

        Set<Long> answeredQuestionIds = answerRepository.findByAttemptId(attemptId).stream()
                .map(AnswerEntity::getQuestionId)
                .collect(Collectors.toSet());

        List<QuestionEntity> questions = questionRepository.findByCategoryId(attempt.getCategoryId());
        Optional<QuestionEntity> next = questions.stream()
                .filter(q -> !answeredQuestionIds.contains(q.getId()))
                .findFirst();

        if (next.isEmpty()) {
            // mark completed if not already
            if (attempt.getCompletedAt() == null) {
                attempt.setCompletedAt(OffsetDateTime.now());
                attemptRepository.save(attempt);
            }
            return ResponseEntity.noContent().build();
        }

        QuestionEntity question = next.get();
        List<QuestionChoiceResponse> choices = questionChoiceRepository.findByQuestionId(question.getId()).stream()
                .map(this::toQuestionChoiceResponse)
                .toList();

        NextQuestionResponse response = NextQuestionResponse.builder()
                .attemptId(attemptId.intValue())
                .question(QuestionResponse.builder()
                        .id(question.getId().intValue())
                        .categoryId(question.getCategoryId().intValue())
                        .questionText(question.getQuestionText())
                        .choices(choices)
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    public AnswerResponse submitAnswer(Long attemptId, SubmitAnswerRequest request) {
        AttemptEntity attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt == null) {
            return null;
        }

        QuestionChoiceEntity choice = questionChoiceRepository.findById(request.getChoiceId().longValue()).orElse(null);
        boolean isCorrect = choice != null && Boolean.TRUE.equals(choice.getIsCorrect());

        AnswerEntity answer = AnswerEntity.builder()
                .attemptId(attemptId)
                .questionId(request.getQuestionId().longValue())
                .choiceId(request.getChoiceId().longValue())
                .answeredAt(OffsetDateTime.now())
                .build();
        AnswerEntity saved = answerRepository.save(answer);

        if (isCorrect) {
            attempt.setCorrectCount(Optional.ofNullable(attempt.getCorrectCount()).orElse(0) + 1);
        }

        int answeredCount = answerRepository.findByAttemptId(attemptId).size();
        if (attempt.getTotalQuestions() != null && attempt.getTotalQuestions() == answeredCount
                && attempt.getCompletedAt() == null) {
            attempt.setCompletedAt(OffsetDateTime.now());
        }

        attemptRepository.save(attempt);

        return AnswerResponse.builder()
                .id(saved.getId().intValue())
                .attemptId(attemptId.intValue())
                .questionId(request.getQuestionId())
                .choiceId(request.getChoiceId())
                .isCorrect(isCorrect)
                .answeredAt(saved.getAnsweredAt())
                .build();
    }

    public AttemptResultResponse getResult(Long attemptId) {
        AttemptEntity attempt = attemptRepository.findById(attemptId).orElse(null);
        if (attempt == null) {
            return null;
        }
        CategoryEntity category = categoryRepository.findById(attempt.getCategoryId()).orElse(null);
        return AttemptResultResponse.builder()
                .attemptId(attemptId.intValue())
                .categoryId(category != null ? category.getId().intValue() : null)
                .categoryName(category != null ? category.getName() : null)
                .totalQuestions(attempt.getTotalQuestions())
                .correctCount(attempt.getCorrectCount())
                .completedAt(attempt.getCompletedAt())
                .build();
    }

    public List<AttemptSummaryResponse> listAttemptSummaries() {
        return attemptRepository.findAll().stream()
                .map(this::toAttemptSummary)
                .toList();
    }

    public List<AnswerDetailResponse> getAnswerDetails(Long attemptId) {
        return answerRepository.findByAttemptId(attemptId).stream()
                .map(this::toAnswerDetail)
                .toList();
    }

    private AttemptSummaryResponse toAttemptSummary(AttemptEntity attempt) {
        CategoryEntity category = categoryRepository.findById(attempt.getCategoryId()).orElse(null);
        return AttemptSummaryResponse.builder()
                .id(attempt.getId().intValue())
                .categoryId(category != null ? category.getId().intValue() : null)
                .categoryName(category != null ? category.getName() : null)
                .totalQuestions(attempt.getTotalQuestions())
                .correctCount(attempt.getCorrectCount())
                .status(resolveStatus(attempt))
                .passed(resolvePassed(attempt))
                .createdAt(attempt.getCreatedAt())
                .completedAt(attempt.getCompletedAt())
                .build();
    }

    private AttemptStartResponse toAttemptStartResponse(AttemptEntity attempt) {
        return AttemptStartResponse.builder()
                .id(attempt.getId().intValue())
                .categoryId(attempt.getCategoryId().intValue())
                .totalQuestions(attempt.getTotalQuestions())
                .correctCount(attempt.getCorrectCount())
                .status(resolveStatus(attempt))
                .passed(resolvePassed(attempt))
                .createdAt(attempt.getCreatedAt())
                .completedAt(attempt.getCompletedAt())
                .build();
    }

    private AnswerDetailResponse toAnswerDetail(AnswerEntity answer) {
        QuestionEntity question = questionRepository.findById(answer.getQuestionId()).orElse(null);
        QuestionChoiceEntity choice = questionChoiceRepository.findById(answer.getChoiceId()).orElse(null);
        return AnswerDetailResponse.builder()
                .answerId(answer.getId().intValue())
                .questionId(question != null ? question.getId().intValue() : null)
                .questionText(question != null ? question.getQuestionText() : null)
                .choiceId(choice != null ? choice.getId().intValue() : null)
                .choiceText(choice != null ? choice.getChoiceText() : null)
                .isCorrect(choice != null && Boolean.TRUE.equals(choice.getIsCorrect()))
                .answeredAt(answer.getAnsweredAt())
                .build();
    }

    private QuestionChoiceResponse toQuestionChoiceResponse(QuestionChoiceEntity entity) {
        return QuestionChoiceResponse.builder()
                .id(entity.getId().intValue())
                .questionId(entity.getQuestionId().intValue())
                .choiceText(entity.getChoiceText())
                .build();
    }

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
}
