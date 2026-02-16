FROM eclipse-temurin:17-jre

WORKDIR /app
COPY target/arcade-hub-server-1.0.0.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
