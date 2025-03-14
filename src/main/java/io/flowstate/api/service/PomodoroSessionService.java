package io.flowstate.api.service;

import io.flowstate.api.dto.pomodoro.PomodoroSessionRequestDto;
import io.flowstate.api.dto.pomodoro.TimerStateDto;
import io.flowstate.api.entity.PomodoroSession;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PomodoroSessionService {
    List<PomodoroSession> getUserSessions(UUID userId);
    List<PomodoroSession> getUserSessionsByTask(UUID userId, UUID taskId);
    List<PomodoroSession> getUserSessionsSince(UUID userId, Instant since);
    PomodoroSession getSessionById(UUID userId, UUID sessionId);
    PomodoroSession getCurrentSession(UUID userId);
    PomodoroSession startSession(UUID userId, PomodoroSessionRequestDto requestDto);
    PomodoroSession completeSession(UUID userId, UUID sessionId);
    PomodoroSession pauseSession(UUID userId, UUID sessionId);
    PomodoroSession resumeSession(UUID userId, UUID sessionId);
    void cancelSession(UUID userId, UUID sessionId);
    TimerStateDto getTimerState(UUID userId);
}
