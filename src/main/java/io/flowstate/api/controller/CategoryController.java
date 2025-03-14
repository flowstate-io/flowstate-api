package io.flowstate.api.controller;

import io.flowstate.api.dto.category.CategoryRequestDto;
import io.flowstate.api.dto.category.CategoryResponseDto;
import io.flowstate.api.mapper.CategoryMapper;
import io.flowstate.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.flowstate.api.util.AuthenticationUtil.getUserIdFromAuthentication;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var categories = categoryService.getAllUserCategories(userId)
                .stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            Authentication authentication,
            @PathVariable UUID categoryId) {
        var userId = getUserIdFromAuthentication(authentication);
        var category = categoryService.getCategoryById(userId, categoryId);
        return ResponseEntity.ok(categoryMapper.toResponseDto(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            Authentication authentication,
            @Valid @RequestBody CategoryRequestDto requestDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var newCategory = categoryService.createCategory(userId, requestDto);
        return ResponseEntity.ok(categoryMapper.toResponseDto(newCategory));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            Authentication authentication,
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequestDto requestDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var updatedCategory = categoryService.updateCategory(userId, categoryId, requestDto);
        return ResponseEntity.ok(categoryMapper.toResponseDto(updatedCategory));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            Authentication authentication,
            @PathVariable UUID categoryId) {
        var userId = getUserIdFromAuthentication(authentication);
        categoryService.deleteCategory(userId, categoryId);
        return ResponseEntity.noContent().build();
    }
}
