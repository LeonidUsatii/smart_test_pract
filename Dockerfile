# Задает базовый образ для сборки. Использует официальный образ Gradle с JDK 17 для сборки проекта.
FROM docker.io/gradle:8.5.0-jdk17 AS build
# Копирует исходный код приложения в образ, устанавливая права пользователя gradle на скопированные файлы.
COPY --chown=gradle:gradle . /app/src
# Устанавливает рабочую директорию для последующих инструкций в Dockerfile.
WORKDIR /app/src
# Выполняет сборку проекта с использованием Gradle без запуска демона Gradle.
RUN gradle build --no-daemon  \
# Задает базовый образ для запуска. Использует официальный образ OpenJDK 17.
FROM openjdk:17-jdk
# Информирует Docker, что контейнер будет слушать порт 8080.
EXPOSE 8080
# Создает директорию /app в образе для хранения исполняемого файла приложения.
RUN mkdir /app \
# Копирует собранный jar-файл из образа сборки в рабочую директорию запуска.
COPY --from=build /app/src/build/libs/smart-test-it-0.0.1-SNAPSHOT.jar /app/application.jar
# Задает команду для запуска приложения.
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
