package io.flowstate.api.service.impl;

import io.flowstate.api.dto.task.TaskRequestDto;
import io.flowstate.api.entity.Category;
import io.flowstate.api.entity.Task;
import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.repository.CategoryRepository;
import io.flowstate.api.repository.TaskRepository;
import io.flowstate.api.repository.UserRepository;
import io.flowstate.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllUserTasks(UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getUserTasksByCompleted(UUID userId, boolean completed) {
        return taskRepository.findByUserIdAndCompletedOrderByPriorityDesc(userId, completed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getUserTasksByCategory(UUID userId, UUID categoryId) {
        return taskRepository.findByUserIdAndCategoryIdOrderByPriorityDesc(userId, categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(UUID userId, UUID taskId) {
        return taskRepository.findById(taskId)
                .filter(task -> task.getUser().getId().equals(userId))
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "Task not found")
                                .build()
                ));
    }

    @Override
    @Transactional
    public Task createTask(UUID userId, TaskRequestDto taskRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "User not found")
                                .withErrorType(ACCOUNT_UNAVAILABLE)
                                .build()
                ));

        Task task = new Task();
        task.setUser(user);
        task.setTitle(taskRequestDto.title());
        task.setDescription(taskRequestDto.description());
        task.setEstimatedPomodoros(taskRequestDto.estimatedPomodoros());
        task.setCompletedPomodoros(0);

        if (taskRequestDto.priority() != null) {
            task.setPriority(taskRequestDto.priority());
        }

        if (taskRequestDto.categoryId() != null) {
            Category category = categoryRepository.findById(taskRequestDto.categoryId())
                    .filter(c -> c.getUser().getId().equals(userId))
                    .orElseThrow(() -> new RestErrorResponseException(
                            forStatusAndDetail(NOT_FOUND, "Category not found")
                                    .build()
                    ));
            task.setCategory(category);
        }

        task.setDueDate(taskRequestDto.dueDate());
        task.setRecurrencePattern(taskRequestDto.recurrencePattern());
        task.setRecurrenceInterval(taskRequestDto.recurrenceInterval());

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateTask(UUID userId, UUID taskId, TaskRequestDto taskRequestDto) {
        Task task = getTaskById(userId, taskId);

        task.setTitle(taskRequestDto.title());
        task.setDescription(taskRequestDto.description());
        task.setEstimatedPomodoros(taskRequestDto.estimatedPomodoros());

        if (taskRequestDto.priority() != null) {
            task.setPriority(taskRequestDto.priority());
        }

        if (taskRequestDto.categoryId() != null) {
            Category category = categoryRepository.findById(taskRequestDto.categoryId())
                    .filter(c -> c.getUser().getId().equals(userId))
                    .orElseThrow(() -> new RestErrorResponseException(
                            forStatusAndDetail(NOT_FOUND, "Category not found")
                                    .build()
                    ));
            task.setCategory(category);
        } else {
            task.setCategory(null);
        }

        task.setDueDate(taskRequestDto.dueDate());
        task.setRecurrencePattern(taskRequestDto.recurrencePattern());
        task.setRecurrenceInterval(taskRequestDto.recurrenceInterval());

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task toggleTaskCompletion(UUID userId, UUID taskId) {
        Task task = getTaskById(userId, taskId);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(UUID userId, UUID taskId) {
        Task task = getTaskById(userId, taskId);
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public void incrementCompletedPomodoros(UUID userId, UUID taskId) {
        Task task = getTaskById(userId, taskId);
        task.setCompletedPomodoros(task.getCompletedPomodoros() + 1);
        taskRepository.save(task);
    }
}
