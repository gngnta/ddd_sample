package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.controller.response.CategoryResponse;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.mapper.CategoryMapper;

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
