package io.flowstate.api.service.impl;

import io.flowstate.api.dto.pomodoro.PomodoroSettingsDto;
import io.flowstate.api.entity.PomodoroSettings;
import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.repository.PomodoroSettingsRepository;
import io.flowstate.api.repository.UserRepository;
import io.flowstate.api.service.PomodoroSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PomodoroSettingsServiceImpl implements PomodoroSettingsService {

    private final PomodoroSettingsRepository pomodoroSettingsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PomodoroSettings getOrCreateSettings(UUID userId) {
        return pomodoroSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
    }

    @Override
    @Transactional
    public PomodoroSettings updateSettings(UUID userId, PomodoroSettingsDto settingsDto) {
        PomodoroSettings settings = pomodoroSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));

        settings.setWorkDuration(Duration.ofMinutes(settingsDto.workDuration()));
        settings.setShortBreakDuration(Duration.ofMinutes(settingsDto.shortBreakDuration()));
        settings.setLongBreakDuration(Duration.ofMinutes(settingsDto.longBreakDuration()));
        settings.setSessionsUntilLongBreak(settingsDto.sessionsUntilLongBreak());
        settings.setAutoStartBreaks(settingsDto.autoStartBreaks());
        settings.setAutoStartPomodoros(settingsDto.autoStartPomodoros());
        settings.setSoundEnabled(settingsDto.soundEnabled());
        settings.setNotificationsEnabled(settingsDto.notificationsEnabled());

        return pomodoroSettingsRepository.save(settings);
    }

    private PomodoroSettings createDefaultSettings(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestErrorResponseException(
                        forStatusAndDetail(NOT_FOUND, "User not found")
                                .withErrorType(ACCOUNT_UNAVAILABLE)
                                .build()
                ));

        PomodoroSettings settings = new PomodoroSettings();
        settings.setUser(user);
        return pomodoroSettingsRepository.save(settings);
    }
}
