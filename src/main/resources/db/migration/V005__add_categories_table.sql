CREATE TABLE categories
(
    id         UUID PRIMARY KEY,
    user_id    UUID                     NOT NULL,
    name       VARCHAR(50)              NOT NULL,
    color      VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_categories_user_id ON categories (user_id);
CREATE UNIQUE INDEX idx_categories_name_user_id ON categories (user_id, name);