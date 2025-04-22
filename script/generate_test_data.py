import argparse
import requests
from faker import Faker
import random
from typing import List, Dict

fake = Faker()

class TestDataGenerator:
    def __init__(self, base_url: str = "http://localhost:8080"):
        self.base_url = base_url.rstrip("/")

    def generate_users(self, count: int) -> List[Dict]:
        """Генерация тестовых пользователей"""
        user_ids = []
        for i in range(count):
            user_data = {
                "fio": fake.name(),
                "email": fake.email(),
                "login": fake.user_name()
            }
            response = requests.post(f"{self.base_url}/users", json=user_data)
            if response.status_code == 201:
                user_ids.append(response.json())
            if (i+1) % 100 == 0:
                print(f"Создано {i+1}/{count} пользователей")
        return user_ids

    def generate_lessons(self, count: int) -> List[Dict]:
        """Генерация тестовых уроков"""
        lesson_ids = []
        for i in range(count):
            lesson_data = {
                "title": fake.sentence(nb_words=3),
                "nameTest": fake.text(max_nb_chars=200),
                "seconds": random.randint(5, 120)
            }
            response = requests.post(f"{self.base_url}/lessons", json=lesson_data)
            if response.status_code == 201:
                lesson_ids.append(response.json())
            if (i+1) % 100 == 0:
                print(f"Создано {i+1}/{count} уроков")
        return lesson_ids

    def generate_progress(self, count: int, users: List[Dict], lessons: List[Dict], clear: bool) -> None:
        """Генерация тестового прогресса"""
        if not users or not lessons:
            print("Ошибка: Нет пользователей или уроков для создания прогресса")
            return

        for i in range(count):
            if clear:
                # Берем последовательные записи при очистке
                user = users[i % len(users)]
                lesson = lessons[i % len(lessons)]
            else:
                # Берем случайные записи
                user = random.choice(users)
                lesson = random.choice(lessons)

            progress_data = {
                "user": user['id'],
                "lesson": lesson['id'],
                "ending": fake.date_time_this_year().isoformat(),
                "testResult": random.randint(0, 100)
            }

            response = requests.post(f"{self.base_url}/progresses", json=progress_data)
            if response.status_code != 201:
                print(f"Ошибка при создании прогресса: {response.text}")

            if (i+1) % 100 == 0:
                print(f"Создано {i+1}/{count} записей прогресса")


def clear_data(base_url: str, endpoint: str) -> None:
    """Универсальная очистка данных через получение и удаление всех записей"""
    try:
        # Получаем все записи
        url = f"{base_url}/{endpoint}"
        if endpoint == "progress":
            url = f"{base_url}/progresses"

        response = requests.get(url)
        if response.status_code == 200:
            # Удаляем каждую запись
            for item in response.json():
                delete_url = f"{url}/{item['id']}"
                requests.delete(delete_url)
            print(f"Данные в {endpoint} успешно очищены.")
        else:
            print(f"Не удалось получить данные для очистки {endpoint}: {response.status_code}")
    except Exception as e:
        print(f"Ошибка при очистке {endpoint}: {str(e)}")


def main():
    parser = argparse.ArgumentParser(description="Генератор тестовых данных для REST-сервиса")
    parser.add_argument("--count", type=int, default=500, help="Количество создаваемых объектов")
    parser.add_argument("--endpoint", required=True,
                       choices=["users", "lessons", "progress"],
                       help="API endpoint для заполнения")
    parser.add_argument("--base-url", default="http://localhost:8080",
                       help="Базовый URL REST-сервиса")
    parser.add_argument('--clear', action='store_true', help='Очистить данные перед заполнением.')
    args = parser.parse_args()
    generator = TestDataGenerator(args.base_url)

    if args.clear:
        clear_data(args.base_url, "progresses")
        clear_data(args.base_url, "lessons")
        clear_data(args.base_url, "users")

    if args.endpoint == "users":
        generator.generate_users(args.count)
    elif args.endpoint == "lessons":
        generator.generate_lessons(args.count)
    elif args.endpoint == "progress":
        # Получаем существующих пользователей и уроки
        try:
            lessons_response = requests.get(f"{args.base_url}/lessons")
            users_response = requests.get(f"{args.base_url}/users")

            lessons = lessons_response.json() if lessons_response.status_code == 200 else []
            users = users_response.json() if users_response.status_code == 200 else []

            # Если нет пользователей или уроков - создаем
            if not users:
                print("Создаем пользователей...")
                users = generator.generate_users(args.count)
                if not users:
                    print("Не удалось создать пользователей")
                    return

            if not lessons:
                print("Создаем уроки...")
                lessons = generator.generate_lessons(args.count)
                if not lessons:
                    print("Не удалось создать уроки")
                    return

            print("Создаем записи прогресса...")
            generator.generate_progress(args.count, users, lessons, args.clear)

        except requests.exceptions.RequestException as e:
            print(f"Ошибка при получении данных: {str(e)}")


if __name__ == "__main__":
    main()