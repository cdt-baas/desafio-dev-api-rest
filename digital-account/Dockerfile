FROM gradle:8.5.0-jdk21-alpine AS build

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=build /app/build/libs/digital-account.jar app.jar

CMD ["java", "-jar", "app.jar"]