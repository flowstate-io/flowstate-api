package io.flowstate.api.service.impl;

import io.flowstate.api.dto.pomodoro.PomodoroSessionRequestDto;
import io.flowstate.api.dto.pomodoro.TimerStateDto;
import io.flowstate.api.entity.PomodoroSession;
import io.flowstate.api.entity.PomodoroSettings;
import io.flowstate.api.entity.SessionType;
import io.flowstate.api.entity.Task;
import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.repository.PomodoroSessionRepository;
import io.flowstate.api.repository.UserRepository;
import io.flowstate.api.service.PomodoroSessionService;
import io.flowstate.api.service.PomodoroSettingsService;
import io.flowstate.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PomodoroSessionServiceImpl implements PomodoroSessionService {

    private final PomodoroSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PomodoroSettingsService pomodoroSettingsService;
    private final TaskService taskService;

    @Override
    @Transactional(readOnly = true)
    public List<PomodoroSession> getUserSessions(UUID userId) {
        return sessionRepository.findByUserIdOrderByStartTimeDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PomodoroSession> getUserSessionsByTask(UUID userId, UUID taskId) {
        return sessionRepository.findByUserIdAndTaskIdOrderByStartTimeDesc(userId, taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PomodoroSession> getUserSessionsSince(UUID userId, Instant since) {
        return sessionRepository.findByUserIdAndStartTimeAfterOrderByStartTimeDesc(userId, since);
    }

    @Override
    @Transactional(readOnly = true)
    public PomodoroSession getSessionById(UUID userId, UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .filter(session -> session.getUser().getId().equals(userId))
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "Session not found")
                                .build()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public PomodoroSession getCurrentSession(UUID userId) {
        return sessionRepository.findFirstByUserIdAndCompletedFalseOrderByStartTimeDesc(userId)
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "No active session found")
                                .build()
                ));
    }

    @Override
    @Transactional
    public PomodoroSession startSession(UUID userId, PomodoroSessionRequestDto requestDto) {
        // Check if there's an active session
        Optional<PomodoroSession> activeSession =
                sessionRepository.findFirstByUserIdAndCompletedFalseOrderByStartTimeDesc(userId);
        if (activeSession.isPresent()) {
            throw new RestErrorResponseException(
                    forStatusAndDetail(BAD_REQUEST, "You already have an active session")
                            .build()
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "User not found")
                                .withErrorType(ACCOUNT_UNAVAILABLE)
                                .build()
                ));

        PomodoroSession session = new PomodoroSession();
        session.setUser(user);
        session.setSessionType(requestDto.sessionType());
        session.setStartTime(Instant.now());

        // Associate with task if provided
        if (requestDto.taskId() != null) {
            Task task = taskService.getTaskById(userId, requestDto.taskId());
            session.setTask(task);
        }

        return sessionRepository.save(session);
    }

    @Override
    @Transactional
    public PomodoroSession completeSession(UUID userId, UUID sessionId) {
        PomodoroSession session = getSessionById(userId, sessionId);

        if (session.isCompleted()) {
            throw new RestErrorResponseException(
                    forStatusAndDetail(BAD_REQUEST, "Session is already completed")
                            .build()
            );
        }

        session.setCompleted(true);
        session.setEndTime(Instant.now());
        session.setDuration(Duration.between(session.getStartTime(), session.getEndTime()));

        // If it's a pomodoro session and associated with a task, increment completed pomodoros
        if (session.getSessionType() == SessionType.POMODORO && session.getTask() != null) {
            taskService.incrementCompletedPomodoros(userId, session.getTask().getId());
        }

        return sessionRepository.save(session);
    }

    @Override
    @Transactional
    public PomodoroSession pauseSession(UUID userId, UUID sessionId) {
        // This would just be a placeholder in a real app that tracks pauses in the UI
        // In a real implementation, you might want to store pause state and time
        return getSessionById(userId, sessionId);
    }

    @Override
    @Transactional
    public PomodoroSession resumeSession(UUID userId, UUID sessionId) {
        // This would just be a placeholder in a real app that tracks pauses in the UI
        // In a real implementation, you might want to update the session after resume
        return getSessionById(userId, sessionId);
    }

    @Override
    @Transactional
    public void cancelSession(UUID userId, UUID sessionId) {
        PomodoroSession session = getSessionById(userId, sessionId);
        sessionRepository.delete(session);
    }

    @Override
    @Transactional(readOnly = true)
    public TimerStateDto getTimerState(UUID userId) {
        Optional<PomodoroSession> activeSessionOpt =
                sessionRepository.findFirstByUserIdAndCompletedFalseOrderByStartTimeDesc(userId);

        if (activeSessionOpt.isEmpty()) {
            return new TimerStateDto(
                    "idle",
                    0,
                    0,
                    0,
                    false
            );
        }

        PomodoroSession activeSession = activeSessionOpt.get();
        PomodoroSettings settings = pomodoroSettingsService.getOrCreateSettings(userId);

        Duration totalDuration;
        switch (activeSession.getSessionType()) {
            case POMODORO -> totalDuration = settings.getWorkDuration();
            case SHORT_BREAK -> totalDuration = settings.getShortBreakDuration();
            case LONG_BREAK -> totalDuration = settings.getLongBreakDuration();
            default -> totalDuration = Duration.ZERO;
        }

        Duration elapsed = Duration.between(activeSession.getStartTime(), Instant.now());
        long timeRemaining = Math.max(0, totalDuration.minus(elapsed).toMillis());

        // Count completed pomodoros for the day
        long sessionsCompleted = sessionRepository.countByUserIdAndTaskIdAndSessionTypeAndCompleted(
                userId, activeSession.getTask() != null ? activeSession.getTask().getId() : null,
                SessionType.POMODORO, true);

        return new TimerStateDto(
                activeSession.getSessionType().toString().toLowerCase(),
                timeRemaining,
                totalDuration.toMillis(),
                (int) sessionsCompleted,
                false // Since we don't actually implement pause state persistence in this example
        );
    }
}
