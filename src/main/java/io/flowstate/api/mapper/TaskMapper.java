package io.flowstate.api.mapper;

import io.flowstate.api.dto.category.CategoryResponseDto;
import io.flowstate.api.dto.task.TaskResponseDto;
import io.flowstate.api.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final CategoryMapper categoryMapper;

    public TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getEstimatedPomodoros(),
                task.getCompletedPomodoros(),
                task.getCategory() != null ? categoryMapper.toResponseDto(task.getCategory()) : null,
                task.getPriority(),
                task.isCompleted(),
                task.getDueDate(),
                task.getRecurrencePattern(),
                task.getRecurrenceInterval(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}