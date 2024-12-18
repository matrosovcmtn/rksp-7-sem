FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/config-server.jar /app
CMD ["java", "-jar", "config-server.jar"]
EXPOSE 8888