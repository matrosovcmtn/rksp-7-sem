FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/service-discovery.jar /app
CMD ["java", "-jar", "service-discovery.jar"]
EXPOSE 8761