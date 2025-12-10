package com.example.demo.application.mapper;

import org.mapstruct.Mapper;

import com.example.demo.infrastructure.entity.CategoryEntity;
import com.example.demo.presentation.response.CategoryResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(CategoryEntity entity);
}
