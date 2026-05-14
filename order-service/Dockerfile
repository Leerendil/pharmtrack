FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY order-service/pom.xml order-service/
COPY order-service/src order-service/src
WORKDIR /app/order-service
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/order-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]