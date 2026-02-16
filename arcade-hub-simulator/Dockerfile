FROM eclipse-temurin:17-jre

WORKDIR /app
COPY target/arcade-hub-simulator-1.0.0-jar-with-dependencies.jar app.jar

ENV API_URL=http://arcade-hub-server:8081

ENTRYPOINT ["java", "-jar", "app.jar"]
