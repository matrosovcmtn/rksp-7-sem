FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/api-gateway.jar /app
CMD ["java", "-jar", "api-gateway.jar"]
EXPOSE 80