FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Gradle wrapper and config files for build
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY .env .

# Copy source code
COPY src src

# Copy wait-for-it.sh script into the image
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x ./gradlew /app/wait-for-it.sh

# Build the Spring Boot fat JAR
RUN ./gradlew clean bootJar --no-daemon

# Expose port 8080 for the app
EXPOSE 8080

# Run wait-for-it.sh to wait for MySQL readiness before launching app
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--timeout=60", "--strict", "--", "java", "-jar", "build/libs/gh-org-tools-0.0.1-SNAPSHOT.jar"]
