package io.flowstate.api.service;

import io.flowstate.api.dto.category.CategoryRequestDto;
import io.flowstate.api.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllUserCategories(UUID userId);
    Category getCategoryById(UUID userId, UUID categoryId);
    Category createCategory(UUID userId, CategoryRequestDto categoryRequestDto);
    Category updateCategory(UUID userId, UUID categoryId, CategoryRequestDto categoryRequestDto);
    void deleteCategory(UUID userId, UUID categoryId);
}
