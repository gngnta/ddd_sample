package com.example.demo.service.mapper;

import org.mapstruct.Mapper;

import com.example.demo.controller.response.CategoryResponse;
import com.example.demo.repository.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(CategoryEntity entity);
}
