package io.flowstate.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pomodoro_settings")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PomodoroSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Duration workDuration = Duration.ofMinutes(25);

    @Column(nullable = false)
    private Duration shortBreakDuration = Duration.ofMinutes(5);

    @Column(nullable = false)
    private Duration longBreakDuration = Duration.ofMinutes(15);

    @Column(nullable = false)
    private int sessionsUntilLongBreak = 4;

    @Column(nullable = false)
    private boolean autoStartBreaks = true;

    @Column(nullable = false)
    private boolean autoStartPomodoros = false;

    @Column(nullable = false)
    private boolean soundEnabled = true;

    @Column(nullable = false)
    private boolean notificationsEnabled = true;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @LastModifiedDate
    private Instant updatedAt;
}
