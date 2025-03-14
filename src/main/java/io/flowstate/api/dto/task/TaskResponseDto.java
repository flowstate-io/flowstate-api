package io.flowstate.api.dto.task;

import io.flowstate.api.dto.category.CategoryResponseDto;
import io.flowstate.api.entity.Priority;
import io.flowstate.api.entity.RecurrencePattern;

import java.time.Instant;
import java.util.UUID;

public record TaskResponseDto(
        UUID id,
        String title,
        String description,
        int estimatedPomodoros,
        int completedPomodoros,
        CategoryResponseDto category,
        Priority priority,
        boolean completed,
        Instant dueDate,
        RecurrencePattern recurrencePattern,
        Integer recurrenceInterval,
        Instant createdAt,
        Instant updatedAt
) {
}