package com.example.demo.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.entity.QuestionChoiceEntity;

@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoiceEntity, Integer> {
    List<QuestionChoiceEntity> findByQuestionId(Integer questionId);
}
