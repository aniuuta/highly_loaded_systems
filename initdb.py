import psycopg2
from faker import Faker
from psycopg2 import Error
from datetime import datetime, timedelta
import uuid

def generate_uuid():
    return str(uuid.uuid4())

def init_database():
    try:
        conn = psycopg2.connect(
            dbname="progress_db",
            user="admin",
            password="admin",
            host="postgres",
            port="5432"
        )
        print("✅ Успешно подключились к базе данных!")

        cur = conn.cursor()

        # Создание таблиц
        cur.execute("""
            CREATE TABLE IF NOT EXISTS users (
                identifier UUID PRIMARY KEY,
                fio VARCHAR(255) NOT NULL,
                login VARCHAR(100) NOT NULL UNIQUE,
                email VARCHAR(255) NOT NULL UNIQUE,
                date_registry TIMESTAMP NOT NULL
            )
        """)

        cur.execute("""
            CREATE TABLE IF NOT EXISTS lessons (
                id UUID PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                seconds INTEGER NOT NULL,
                name_test VARCHAR(255) NOT NULL
            )
        """)

        cur.execute("""
            CREATE TABLE IF NOT EXISTS progress (
                id UUID PRIMARY KEY,
                user_id UUID NOT NULL REFERENCES users(identifier),
                lesson_id UUID NOT NULL REFERENCES lessons(id),
                ending TIMESTAMP NOT NULL,
                test_result INTEGER NOT NULL DEFAULT 0
            )
        """)

        fake = Faker('ru_RU')

        # Генерация уроков
        lesson_titles = [
            "Введение в Java",
            "Основы Spring Framework",
            "REST API с Spring Boot",
            "Работа с базами данных",
            "Микросервисная архитектура"
        ]

        lessons_ids = []
        for title in lesson_titles:
            lesson_id = generate_uuid()
            lessons_ids.append(lesson_id)
            seconds = fake.random_int(min=1800, max=7200)  # 30-120 минут
            test_name = f"Тест: {title}"

            cur.execute(
                "INSERT INTO lessons (id, title, seconds, name_test) VALUES (%s, %s, %s, %s)",
                (lesson_id, title, seconds, test_name)
            )
            print(f"✨ Добавлен урок: {title}")

        # Генерация пользователей
        users_ids = []
        for _ in range(10):
            try:
                user_id = generate_uuid()
                users_ids.append(user_id)
                fio = fake.name()
                login = fake.user_name()
                email = fake.email()
                date_registry = fake.date_time_between(
                    start_date='-1y',
                    end_date='now'
                )

                cur.execute(
                    "INSERT INTO users (identifier, fio, login, email, date_registry) VALUES (%s, %s, %s, %s, %s)",
                    (user_id, fio, login, email, date_registry)
                )
                print(f"✨ Добавлен пользователь: {fio} ({email})")

            except psycopg2.IntegrityError:
                conn.rollback()
                continue

        # Генерация прогресса
        progress_count = 0
        for user_id in users_ids:
            # Для каждого пользователя генерируем прогресс по случайным урокам
            for _ in range(fake.random_int(min=1, max=len(lessons_ids))):
                lesson_id = fake.random_element(lessons_ids)
                ending = fake.date_time_between(
                    start_date='-6m',
                    end_date='now'
                )
                test_result = fake.random_int(min=0, max=100)

                try:
                    cur.execute(
                        "INSERT INTO progress (id, user_id, lesson_id, ending, test_result) VALUES (%s, %s, %s, %s, %s)",
                        (generate_uuid(), user_id, lesson_id, ending, test_result)
                    )
                    progress_count += 1
                    print(f"✨ Добавлен прогресс для пользователя {user_id[:8]}... по уроку {lesson_id[:8]}...")

                except psycopg2.IntegrityError:
                    conn.rollback()
                    continue

        conn.commit()
        print(f"\n🎉 Успешно добавлено:")
        print(f"👤 Пользователей: {len(users_ids)}")
        print(f"📚 Уроков: {len(lessons_ids)}")
        print(f"📊 Записей прогресса: {progress_count}")

    except (Exception, Error) as error:
        print(f"❌ Ошибка при работе с PostgreSQL: {error}")

    finally:
        if conn:
            cur.close()
            conn.close()
            print("🔒 Соединение с базой данных закрыто")

if __name__ == "__main__":
    init_database()