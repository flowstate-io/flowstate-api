package io.flowstate.api.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
        @NotBlank(message = "Category name is required")
        @Size(min = 1, max = 50, message = "Category name must be between 1 and 50 characters")
        String name,

        String color
) {
}
