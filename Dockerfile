# Multi-stage build for Book Store Management System
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy CSV file and create reports directory
COPY src/main/resources/books_catalog.csv /app/books_catalog.csv
RUN mkdir -p /app/reports

# Expose port
EXPOSE 8000

# Set environment variables with defaults
ENV CSV_FILE_PATH=/app/books_catalog.csv
ENV REPORT_FILE_PATH=/app/reports/book-report.txt
ENV SERVER_PORT=8000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
