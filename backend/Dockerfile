# Backend Dockerfile for Development
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Install dos2unix to handle Windows CRLF line endings
RUN apk add --no-cache dos2unix

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# Convert CRLF to LF and give execution permission to gradlew
RUN dos2unix gradlew && chmod +x gradlew

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src/

# Expose Spring Boot port
EXPOSE 8080

# Start Spring Boot application in development mode
CMD ["./gradlew", "bootRun", "--no-daemon"]
