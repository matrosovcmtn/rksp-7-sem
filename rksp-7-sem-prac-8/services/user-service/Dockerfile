FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/user-service.jar /app
CMD ["java", "-jar", "user-service.jar"]
EXPOSE 8080