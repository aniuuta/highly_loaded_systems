FROM openjdk:21-jdk-slim

# Создаём пользователя без привилегий
RUN useradd -m -s /usr/sbin/nologin appuser

WORKDIR /app

# Копируем файлы с правами нового пользователя
COPY --chown=appuser:appuser build /app/build
COPY --chown=appuser:appuser build/libs/*.jar app.jar

# Делаем файлы доступными только для чтения
RUN chmod -R 744 /app
RUN chown -R appuser /app
# Переключаемся на пользователя с ограниченными правами
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]