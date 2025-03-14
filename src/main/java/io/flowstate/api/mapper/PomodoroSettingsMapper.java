package io.flowstate.api.mapper;

import io.flowstate.api.dto.pomodoro.PomodoroSettingsDto;
import io.flowstate.api.entity.PomodoroSettings;
import org.springframework.stereotype.Component;

@Component
public class PomodoroSettingsMapper {

    public PomodoroSettingsDto toDto(PomodoroSettings settings) {
        return new PomodoroSettingsDto(
                (int) settings.getWorkDuration().toMinutes(),
                (int) settings.getShortBreakDuration().toMinutes(),
                (int) settings.getLongBreakDuration().toMinutes(),
                settings.getSessionsUntilLongBreak(),
                settings.isAutoStartBreaks(),
                settings.isAutoStartPomodoros(),
                settings.isSoundEnabled(),
                settings.isNotificationsEnabled()
        );
    }
}