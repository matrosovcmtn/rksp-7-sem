FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/delivery-service.jar /app
CMD ["java", "-jar", "delivery-service.jar"]
EXPOSE 8082