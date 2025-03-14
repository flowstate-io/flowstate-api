package io.flowstate.api.dto.pomodoro;

import io.flowstate.api.entity.SessionType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PomodoroSessionRequestDto(
        UUID taskId,
        @NotNull SessionType sessionType
) {
}
