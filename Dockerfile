FROM gradle:8.10.2-jdk17 AS build

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./

COPY src src

RUN ./gradlew build -x test

FROM eclipse-temurin:17-jdk AS run

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
