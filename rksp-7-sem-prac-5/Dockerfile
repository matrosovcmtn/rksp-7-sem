# Matrosov Danil IKBO-01-21
FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]