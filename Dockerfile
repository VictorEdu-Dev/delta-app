# Stage 1: build do JAR com Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: imagem final com JRE
FROM eclipse-temurin:21-jdk
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/delta-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]