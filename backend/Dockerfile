FROM openjdk:21-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy pom.xml first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests && \
    cp target/calculator-backend-1.0.0.jar app.jar

# Change ownership to spring user
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

# Optimize JVM for containers
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]