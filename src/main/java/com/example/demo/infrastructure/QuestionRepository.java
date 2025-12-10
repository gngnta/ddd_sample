package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    List<QuestionEntity> findByCategoryId(Integer categoryId);

    @Query("SELECT q FROM QuestionEntity q " +
            "WHERE q.categoryId = :categoryId " +
            "AND q.id NOT IN (SELECT a.questionId FROM AnswerEntity a WHERE a.attemptId = :attemptId) " +
            "ORDER BY FUNCTION('RANDOM') FETCH FIRST 1 ROW ONLY")
    Optional<QuestionEntity> findRandomQuestionExcludingAnswered(Integer attemptId, Integer categoryId);

    long countByCategoryId(Integer categoryId);
}
