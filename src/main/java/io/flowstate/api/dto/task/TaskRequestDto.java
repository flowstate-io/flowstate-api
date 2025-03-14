package io.flowstate.api.dto.task;

import io.flowstate.api.entity.Priority;
import io.flowstate.api.entity.RecurrencePattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record TaskRequestDto(
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
        String title,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Estimated pomodoros is required")
        @Min(value = 1, message = "Estimated pomodoros must be at least 1")
        Integer estimatedPomodoros,

        UUID categoryId,

        Priority priority,

        Instant dueDate,

        RecurrencePattern recurrencePattern,

        Integer recurrenceInterval
) {
}
