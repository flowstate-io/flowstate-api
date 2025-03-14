CREATE TABLE pomodoro_settings
(
    id                        UUID PRIMARY KEY,
    user_id                   UUID                     NOT NULL UNIQUE,
    work_duration             BIGINT                   NOT NULL DEFAULT 1500000000000, -- 25 minutes in nanos
    short_break_duration      BIGINT                   NOT NULL DEFAULT 300000000000,  -- 5 minutes in nanos
    long_break_duration       BIGINT                   NOT NULL DEFAULT 900000000000,  -- 15 minutes in nanos
    sessions_until_long_break INT                      NOT NULL DEFAULT 4,
    auto_start_breaks         BOOLEAN                  NOT NULL DEFAULT TRUE,
    auto_start_pomodoros      BOOLEAN                  NOT NULL DEFAULT FALSE,
    sound_enabled             BOOLEAN                  NOT NULL DEFAULT TRUE,
    notifications_enabled     BOOLEAN                  NOT NULL DEFAULT TRUE,
    updated_at                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pomodoro_settings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);