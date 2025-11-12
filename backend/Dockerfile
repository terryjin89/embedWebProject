# Backend Dockerfile for Development
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# Give execution permission to gradlew
RUN chmod +x gradlew

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src/

# Expose Spring Boot port
EXPOSE 8080

# Start Spring Boot application in development mode
CMD ["./gradlew", "bootRun", "--no-daemon"]
