CREATE TABLE pomodoro_sessions
(
    id           UUID PRIMARY KEY,
    user_id      UUID                     NOT NULL,
    task_id      UUID,
    start_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time     TIMESTAMP WITH TIME ZONE,
    duration     BIGINT,
    session_type VARCHAR(20)              NOT NULL,
    completed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pomodoro_session_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_pomodoro_session_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE SET NULL
);

CREATE INDEX idx_pomodoro_sessions_user_id ON pomodoro_sessions (user_id);
CREATE INDEX idx_pomodoro_sessions_task_id ON pomodoro_sessions (task_id);
CREATE INDEX idx_pomodoro_sessions_start_time ON pomodoro_sessions (start_time);
CREATE INDEX idx_pomodoro_sessions_completed ON pomodoro_sessions (completed);