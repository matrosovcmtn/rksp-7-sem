FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/pizza-service.jar /app
CMD ["java", "-jar", "pizza-service.jar"]
EXPOSE 8081