package com.example.demo.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.infrastructure.entity.AnswerEntity;
import com.example.demo.infrastructure.entity.AttemptEntity;
import com.example.demo.infrastructure.entity.CategoryEntity;
import com.example.demo.infrastructure.entity.QuestionChoiceEntity;
import com.example.demo.infrastructure.entity.QuestionEntity;
import com.example.demo.presentation.response.AnswerDetailResponse;
import com.example.demo.presentation.response.AttemptStartResponse;
import com.example.demo.presentation.response.AttemptSummaryResponse;
import com.example.demo.presentation.response.QuestionChoiceResponse;

@Mapper(componentModel = "spring")
public interface AttemptMapper {

    @Mapping(source = "attempt.id", target = "id")
    @Mapping(source = "category.name", target = "categoryName")
    AttemptSummaryResponse toAttemptSummary(AttemptEntity attempt, CategoryEntity category, String status,
            Boolean passed, Long durationSeconds);

    AttemptStartResponse toAttemptStart(AttemptEntity attempt, String status, Boolean passed);

    QuestionChoiceResponse toQuestionChoiceResponse(QuestionChoiceEntity entity);

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "choice.id", target = "choiceId")
    AnswerDetailResponse toAnswerDetail(AnswerEntity answer, QuestionEntity question, QuestionChoiceEntity choice);
}
