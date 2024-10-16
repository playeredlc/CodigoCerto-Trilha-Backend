FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/*.jar
WORKDIR /app

COPY src /app/src
COPY src/main/resources/db/task-api-codigo-certo.db /data/task-api-codigo-certo.db

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]