package com.example.demo.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.infrastructure.entity.AttemptEntity;

@Repository
public interface AttemptRepository extends JpaRepository<AttemptEntity, Integer> {
}
