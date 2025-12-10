package com.example.demo.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.entity.AnswerEntity;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Integer> {
    List<AnswerEntity> findByAttemptId(Integer attemptId);

    long countByAttemptId(Integer attemptId);
}
