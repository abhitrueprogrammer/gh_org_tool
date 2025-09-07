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

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the Spring Boot fat JAR
RUN ./gradlew clean bootJar --no-daemon

# Expose port 8080 for the app
EXPOSE 8080

# Run the generated Spring Boot fat JAR after build
ENTRYPOINT ["java", "-jar", "build/libs/gh-org-tools-0.0.1-SNAPSHOT.jar"]
