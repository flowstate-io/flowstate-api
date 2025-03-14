package io.flowstate.api.repository;

import io.flowstate.api.entity.PomodoroSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PomodoroSettingsRepository extends JpaRepository<PomodoroSettings, UUID> {
    Optional<PomodoroSettings> findByUserId(UUID userId);
}
