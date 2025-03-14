package io.flowstate.api.mapper;

import io.flowstate.api.dto.pomodoro.PomodoroSessionResponseDto;
import io.flowstate.api.entity.PomodoroSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PomodoroSessionMapper {

    private final TaskMapper taskMapper;

    public PomodoroSessionResponseDto toResponseDto(PomodoroSession session) {
        return new PomodoroSessionResponseDto(
                session.getId(),
                session.getTask() != null ? taskMapper.toResponseDto(session.getTask()) : null,
                session.getStartTime(),
                session.getEndTime(),
                session.getDuration(),
                session.getSessionType(),
                session.isCompleted()
        );
    }
}
