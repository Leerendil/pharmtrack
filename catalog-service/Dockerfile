FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY catalog-service/pom.xml catalog-service/
COPY catalog-service/src catalog-service/src
WORKDIR /app/catalog-service
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/catalog-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
