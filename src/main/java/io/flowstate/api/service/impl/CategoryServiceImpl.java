package io.flowstate.api.service.impl;

import io.flowstate.api.dto.category.CategoryRequestDto;
import io.flowstate.api.entity.Category;
import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.repository.CategoryRepository;
import io.flowstate.api.repository.UserRepository;
import io.flowstate.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ErrorType.RESOURCE_ALREADY_EXISTS;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllUserCategories(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID userId, UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .filter(category -> category.getUser().getId().equals(userId))
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "Category not found")
                                .build()
                ));
    }

    @Override
    @Transactional
    public Category createCategory(UUID userId, CategoryRequestDto categoryRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "User not found")
                                .withErrorType(ACCOUNT_UNAVAILABLE)
                                .build()
                ));

        if (categoryRepository.existsByNameAndUserId(categoryRequestDto.name(), userId)) {
            throw new RestErrorResponseException(
                    forStatusAndDetail(CONFLICT, "Category with this name already exists")
                            .withErrorType(RESOURCE_ALREADY_EXISTS)
                            .build()
            );
        }

        Category category = new Category();
        category.setUser(user);
        category.setName(categoryRequestDto.name());
        category.setColor(categoryRequestDto.color());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(UUID userId, UUID categoryId, CategoryRequestDto categoryRequestDto) {
        Category category = getCategoryById(userId, categoryId);

        // Check for name conflict only if name is being changed
        if (!category.getName().equals(categoryRequestDto.name()) &&
                categoryRepository.existsByNameAndUserId(categoryRequestDto.name(), userId)) {
            throw new RestErrorResponseException(
                    forStatusAndDetail(CONFLICT, "Category with this name already exists")
                            .withErrorType(RESOURCE_ALREADY_EXISTS)
                            .build()
            );
        }

        category.setName(categoryRequestDto.name());
        category.setColor(categoryRequestDto.color());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID userId, UUID categoryId) {
        Category category = getCategoryById(userId, categoryId);
        categoryRepository.delete(category);
    }
}
