-- Создание таблицы пользователей
CREATE TABLE users (
    identifier UUID PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    login VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_registry TIMESTAMP NOT NULL
);

-- Создание таблицы уроков
CREATE TABLE lessons (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    seconds INTEGER NOT NULL,
    name_test VARCHAR(255) NOT NULL
);

-- Создание таблицы прогресса
CREATE TABLE progress (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(identifier),
    lesson_id UUID NOT NULL REFERENCES lessons(id),
    ending TIMESTAMP NOT NULL,
    test_result INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(identifier) ON DELETE CASCADE,
    CONSTRAINT fk_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

-- Создание индексов для оптимизации
CREATE INDEX idx_users_login ON users(login);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_progress_user ON progress(user_id);
CREATE INDEX idx_progress_lesson ON progress(lesson_id);

-- Добавление комментариев к таблицам
COMMENT ON TABLE users IS 'Таблица пользователей системы';
COMMENT ON TABLE lessons IS 'Таблица уроков';
COMMENT ON TABLE progress IS 'Таблица прогресса пользователей по урокам';

-- Добавление тестовых данных (опционально)
INSERT INTO users (identifier, fio, login, email, date_registry) VALUES
    (gen_random_uuid(), 'Иванов Иван Иванович', 'ivanov', 'ivanov@example.com', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Петров Петр Петрович', 'petrov', 'petrov@example.com', CURRENT_TIMESTAMP);

INSERT INTO lessons (id, title, seconds, name_test) VALUES
    (gen_random_uuid(), 'Введение в Java', 3600, 'Java Basic Test'),
    (gen_random_uuid(), 'Spring Framework', 4800, 'Spring Basic Test');