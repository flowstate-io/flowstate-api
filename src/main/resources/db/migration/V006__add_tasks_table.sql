CREATE TABLE tasks
(
    id                  UUID PRIMARY KEY,
    user_id             UUID                     NOT NULL,
    title               VARCHAR(100)             NOT NULL,
    description         VARCHAR(1000),
    estimated_pomodoros INT                      NOT NULL DEFAULT 1,
    completed_pomodoros INT                      NOT NULL DEFAULT 0,
    category_id         UUID,
    priority            VARCHAR(20)              NOT NULL DEFAULT 'MEDIUM',
    completed           BOOLEAN                  NOT NULL DEFAULT FALSE,
    due_date            TIMESTAMP WITH TIME ZONE,
    recurrence_pattern  VARCHAR(20),
    recurrence_interval INT,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL
);

CREATE INDEX idx_tasks_user_id ON tasks (user_id);
CREATE INDEX idx_tasks_category_id ON tasks (category_id);
CREATE INDEX idx_tasks_completed ON tasks (completed);
CREATE INDEX idx_tasks_priority ON tasks (priority);