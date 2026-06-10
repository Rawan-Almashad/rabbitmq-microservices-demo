FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl
COPY target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]