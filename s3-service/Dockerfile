FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/s3-service-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]