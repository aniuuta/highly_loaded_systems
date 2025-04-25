-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    login VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_registry TIMESTAMP NOT NULL
);

-- Таблица уроков
CREATE TABLE IF NOT EXISTS lessons (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    seconds INTEGER NOT NULL,
    name_test VARCHAR(255) NOT NULL
);

-- Таблица прогресса пользователей по урокам
CREATE TABLE IF NOT EXISTS progress (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id INTEGER NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    ending TIMESTAMP NOT NULL,
    test_result INTEGER NOT NULL DEFAULT 0
);
