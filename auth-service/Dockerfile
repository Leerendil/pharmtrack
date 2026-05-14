FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY auth-service/pom.xml auth-service/
COPY auth-service/src auth-service/src
WORKDIR /app/auth-service
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
