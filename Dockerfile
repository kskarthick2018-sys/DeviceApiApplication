# Use Temurin JDK 21 (recommended for Spring Boot 3+)
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven files first for caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached layer)
RUN ./mvnw -q dependency:go-offline

# Copy full source
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# ----------- Runtime Image -----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
