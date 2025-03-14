package io.flowstate.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int estimatedPomodoros;

    @Column(nullable = false)
    private int completedPomodoros;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(nullable = false)
    private boolean completed = false;

    @Column
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column
    private RecurrencePattern recurrencePattern;

    @Column
    private Integer recurrenceInterval;

    @OneToMany(mappedBy = "task")
    private List<PomodoroSession> pomodoroSessions;

    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.PRIVATE)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @LastModifiedDate
    private Instant updatedAt;
}
