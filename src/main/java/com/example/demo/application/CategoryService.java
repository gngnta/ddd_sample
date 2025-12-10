package com.example.demo.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.mapper.CategoryMapper;
import com.example.demo.infrastructure.CategoryRepository;
import com.example.demo.presentation.response.CategoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
