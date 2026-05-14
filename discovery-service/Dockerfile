FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY discovery-service/pom.xml discovery-service/
COPY discovery-service/src discovery-service/src
WORKDIR /app/discovery-service
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/discovery-service/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]