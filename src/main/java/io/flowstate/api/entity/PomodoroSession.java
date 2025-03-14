package io.flowstate.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pomodoro_sessions")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PomodoroSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false)
    private Instant startTime;

    @Column
    private Instant endTime;

    @Column
    private Duration duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.PRIVATE)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @LastModifiedDate
    private Instant updatedAt;
}
