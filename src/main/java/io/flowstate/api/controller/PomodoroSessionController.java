package io.flowstate.api.controller;

import io.flowstate.api.dto.pomodoro.PomodoroSessionRequestDto;
import io.flowstate.api.dto.pomodoro.PomodoroSessionResponseDto;
import io.flowstate.api.dto.pomodoro.TimerStateDto;
import io.flowstate.api.mapper.PomodoroSessionMapper;
import io.flowstate.api.service.PomodoroSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.flowstate.api.util.AuthenticationUtil.getUserIdFromAuthentication;

@RestController
@RequestMapping("/api/pomodoro/sessions")
@RequiredArgsConstructor
public class PomodoroSessionController {

    private final PomodoroSessionService sessionService;
    private final PomodoroSessionMapper sessionMapper;

    @GetMapping
    public ResponseEntity<List<PomodoroSessionResponseDto>> getAllSessions(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var sessions = sessionService.getUserSessions(userId)
                .stream()
                .map(sessionMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/today")
    public ResponseEntity<List<PomodoroSessionResponseDto>> getTodaySessions(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var startOfDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        var sessions = sessionService.getUserSessionsSince(userId, startOfDay)
                .stream()
                .map(sessionMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<PomodoroSessionResponseDto>> getSessionsByTask(
            Authentication authentication,
            @PathVariable UUID taskId) {
        var userId = getUserIdFromAuthentication(authentication);
        var sessions = sessionService.getUserSessionsByTask(userId, taskId)
                .stream()
                .map(sessionMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/current")
    public ResponseEntity<PomodoroSessionResponseDto> getCurrentSession(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var session = sessionService.getCurrentSession(userId);
        return ResponseEntity.ok(sessionMapper.toResponseDto(session));
    }

    @GetMapping("/timer-state")
    public ResponseEntity<TimerStateDto> getTimerState(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var timerState = sessionService.getTimerState(userId);
        return ResponseEntity.ok(timerState);
    }

    @PostMapping("/start")
    public ResponseEntity<PomodoroSessionResponseDto> startSession(
            Authentication authentication,
            @Valid @RequestBody PomodoroSessionRequestDto requestDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var session = sessionService.startSession(userId, requestDto);
        return ResponseEntity.ok(sessionMapper.toResponseDto(session));
    }

    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<PomodoroSessionResponseDto> completeSession(
            Authentication authentication,
            @PathVariable UUID sessionId) {
        var userId = getUserIdFromAuthentication(authentication);
        var session = sessionService.completeSession(userId, sessionId);
        return ResponseEntity.ok(sessionMapper.toResponseDto(session));
    }

    @PostMapping("/{sessionId}/pause")
    public ResponseEntity<PomodoroSessionResponseDto> pauseSession(
            Authentication authentication,
            @PathVariable UUID sessionId) {
        var userId = getUserIdFromAuthentication(authentication);
        var session = sessionService.pauseSession(userId, sessionId);
        return ResponseEntity.ok(sessionMapper.toResponseDto(session));
    }

    @PostMapping("/{sessionId}/resume")
    public ResponseEntity<PomodoroSessionResponseDto> resumeSession(
            Authentication authentication,
            @PathVariable UUID sessionId) {
        var userId = getUserIdFromAuthentication(authentication);
        var session = sessionService.resumeSession(userId, sessionId);
        return ResponseEntity.ok(sessionMapper.toResponseDto(session));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancelSession(
            Authentication authentication,
            @PathVariable UUID sessionId) {
        var userId = getUserIdFromAuthentication(authentication);
        sessionService.cancelSession(userId, sessionId);
        return ResponseEntity.noContent().build();
    }
}
