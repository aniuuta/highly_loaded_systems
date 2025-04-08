import psycopg2
from faker import Faker

# Подключение к базе данных
conn = psycopg2.connect(
    dbname="your_database_name",
    user="your_username",
    password="your_password",
    host="postgres",  # Имя сервиса в docker-compose
    port="5432"
)

# Создание курсора
cur = conn.cursor()

# Генерация данных с помощью Faker
fake = Faker()

# Создание таблицы (если её нет)
cur.execute("""
    CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100),
        email VARCHAR(100)
    )
""")

# Вставка тестовых данных
for _ in range(10):  # Генерация 10 записей
    name = fake.name()
    email = fake.email()
    cur.execute("INSERT INTO users (name, email) VALUES (%s, %s)", (name, email))

# Фиксация изменений и закрытие соединения
conn.commit()
cur.close()
conn.close()

print("Database initialized with test data.")