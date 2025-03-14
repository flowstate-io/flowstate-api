package io.flowstate.api.controller;

import io.flowstate.api.dto.pomodoro.PomodoroSettingsDto;
import io.flowstate.api.mapper.PomodoroSettingsMapper;
import io.flowstate.api.service.PomodoroSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static io.flowstate.api.util.AuthenticationUtil.getUserIdFromAuthentication;

@RestController
@RequestMapping("/api/pomodoro/settings")
@RequiredArgsConstructor
public class PomodoroSettingsController {

    private final PomodoroSettingsService pomodoroSettingsService;
    private final PomodoroSettingsMapper pomodoroSettingsMapper;

    @GetMapping
    public ResponseEntity<PomodoroSettingsDto> getSettings(Authentication authentication) {
        var userId = getUserIdFromAuthentication(authentication);
        var settings = pomodoroSettingsService.getOrCreateSettings(userId);
        return ResponseEntity.ok(pomodoroSettingsMapper.toDto(settings));
    }

    @PutMapping
    public ResponseEntity<PomodoroSettingsDto> updateSettings(
            Authentication authentication,
            @Valid @RequestBody PomodoroSettingsDto settingsDto) {
        var userId = getUserIdFromAuthentication(authentication);
        var updatedSettings = pomodoroSettingsService.updateSettings(userId, settingsDto);
        return ResponseEntity.ok(pomodoroSettingsMapper.toDto(updatedSettings));
    }
}

