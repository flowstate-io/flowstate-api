package io.flowstate.api.repository;

import io.flowstate.api.entity.PomodoroSession;
import io.flowstate.api.entity.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, UUID> {
    List<PomodoroSession> findByUserIdOrderByStartTimeDesc(UUID userId);
    List<PomodoroSession> findByUserIdAndTaskIdOrderByStartTimeDesc(UUID userId, UUID taskId);
    List<PomodoroSession> findByUserIdAndStartTimeAfterOrderByStartTimeDesc(UUID userId, Instant startTime);
    Optional<PomodoroSession> findFirstByUserIdAndCompletedFalseOrderByStartTimeDesc(UUID userId);
    long countByUserIdAndTaskIdAndSessionTypeAndCompleted(UUID userId, UUID taskId, SessionType sessionType, boolean completed);
}
