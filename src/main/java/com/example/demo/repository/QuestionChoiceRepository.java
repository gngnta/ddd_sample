package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.QuestionChoiceEntity;

@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoiceEntity, Long> {
}
