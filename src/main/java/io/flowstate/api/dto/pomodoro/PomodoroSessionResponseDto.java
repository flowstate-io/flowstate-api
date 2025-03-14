package io.flowstate.api.dto.pomodoro;

import io.flowstate.api.dto.task.TaskResponseDto;
import io.flowstate.api.entity.SessionType;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record PomodoroSessionResponseDto(
        UUID id,
        TaskResponseDto task,
        Instant startTime,
        Instant endTime,
        Duration duration,
        SessionType sessionType,
        boolean completed
) {
}
