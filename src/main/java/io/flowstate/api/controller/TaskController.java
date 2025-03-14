package io.flowstate.api.controller;

import io.flowstate.api.dto.task.TaskRequestDto;
import io.flowstate.api.dto.task.TaskResponseDto;
import io.flowstate.api.mapper.TaskMapper;
import io.flowstate.api.service.TaskService;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var tasks = taskService.getAllUserTasks(userId)
                .stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TaskResponseDto>> getActiveTasks(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var tasks = taskService.getUserTasksByCompleted(userId, false)
                .stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TaskResponseDto>> getCompletedTasks(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var tasks = taskService.getUserTasksByCompleted(userId, true)
                .stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCategory(
            Authentication authentication,
            @PathVariable UUID categoryId) {
        var userId = getUserIdFromAuthentication(authentication);
        var tasks = taskService.getUserTasksByCategory(userId, categoryId)
                .stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(
            Authentication authentication,
            @PathVariable UUID taskId) {
        var userId = getUserIdFromAuthentication(authentication);
        var task = taskService.getTaskById(userId, taskId);
        return ResponseEntity.ok(taskMapper.toResponseDto(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            Authentication authentication,
            @Valid @RequestBody TaskRequestDto requestDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var newTask = taskService.createTask(userId, requestDto);
        return ResponseEntity.ok(taskMapper.toResponseDto(newTask));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(
            Authentication authentication,
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskRequestDto requestDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var updatedTask = taskService.updateTask(userId, taskId, requestDto);
        return ResponseEntity.ok(taskMapper.toResponseDto(updatedTask));
    }

    @PostMapping("/{taskId}/toggle")
    public ResponseEntity<TaskResponseDto> toggleTaskCompletion(
            Authentication authentication,
            @PathVariable UUID taskId) {
        var userId = getUserIdFromAuthentication(authentication);
        var task = taskService.toggleTaskCompletion(userId, taskId);
        return ResponseEntity.ok(taskMapper.toResponseDto(task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            Authentication authentication,
            @PathVariable UUID taskId) {
        var userId = getUserIdFromAuthentication(authentication);
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }
}