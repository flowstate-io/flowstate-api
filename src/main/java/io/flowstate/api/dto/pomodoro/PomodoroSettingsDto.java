package io.flowstate.api.dto.pomodoro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PomodoroSettingsDto(
        @NotNull @Min(1) Integer workDuration,
        @NotNull @Min(1) Integer shortBreakDuration,
        @NotNull @Min(1) Integer longBreakDuration,
        @NotNull @Min(1) Integer sessionsUntilLongBreak,
        @NotNull Boolean autoStartBreaks,
        @NotNull Boolean autoStartPomodoros,
        @NotNull Boolean soundEnabled,
        @NotNull Boolean notificationsEnabled
) {
}
