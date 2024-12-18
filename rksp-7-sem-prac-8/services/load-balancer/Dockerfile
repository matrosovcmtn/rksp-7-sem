FROM amazoncorretto:21.0.5-alpine
WORKDIR /app
COPY /build/libs/load-balancer.jar /app
CMD ["java", "-jar", "load-balancer.jar"]
EXPOSE 9000