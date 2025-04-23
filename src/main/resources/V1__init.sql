-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
    identifier UUID PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    login VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_registry TIMESTAMP NOT NULL
);

-- Создание таблицы уроков
CREATE TABLE IF NOT EXISTS lessons (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    seconds INTEGER NOT NULL,
    name_test VARCHAR(255) NOT NULL
);

-- Создание таблицы прогресса
CREATE TABLE IF NOT EXISTS progress (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(identifier) ON DELETE CASCADE,
    lesson_id UUID NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    ending TIMESTAMP NOT NULL,
    test_result INTEGER NOT NULL DEFAULT 0
);
