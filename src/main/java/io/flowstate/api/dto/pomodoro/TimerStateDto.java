package io.flowstate.api.dto.pomodoro;

public record TimerStateDto(
        String currentState,
        long timeRemaining,
        long totalDuration,
        int sessionsCompleted,
        boolean isPaused
) {
}
