package io.flowstate.api.service;

import io.flowstate.api.dto.task.TaskRequestDto;
import io.flowstate.api.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<Task> getAllUserTasks(UUID userId);
    List<Task> getUserTasksByCompleted(UUID userId, boolean completed);
    List<Task> getUserTasksByCategory(UUID userId, UUID categoryId);
    Task getTaskById(UUID userId, UUID taskId);
    Task createTask(UUID userId, TaskRequestDto taskRequestDto);
    Task updateTask(UUID userId, UUID taskId, TaskRequestDto taskRequestDto);
    Task toggleTaskCompletion(UUID userId, UUID taskId);
    void deleteTask(UUID userId, UUID taskId);
    void incrementCompletedPomodoros(UUID userId, UUID taskId);
}
