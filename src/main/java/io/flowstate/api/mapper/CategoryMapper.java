package io.flowstate.api.mapper;

import io.flowstate.api.dto.category.CategoryResponseDto;
import io.flowstate.api.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto toResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getColor()
        );
    }
}
