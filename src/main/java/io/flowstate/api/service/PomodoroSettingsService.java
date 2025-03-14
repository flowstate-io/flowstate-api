package io.flowstate.api.service;

import io.flowstate.api.dto.pomodoro.PomodoroSettingsDto;
import io.flowstate.api.entity.PomodoroSettings;

import java.util.UUID;

public interface PomodoroSettingsService {
    PomodoroSettings getOrCreateSettings(UUID userId);
    PomodoroSettings updateSettings(UUID userId, PomodoroSettingsDto settingsDto);
}
