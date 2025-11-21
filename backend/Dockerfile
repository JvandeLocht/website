FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Install Maven, wget, xz-utils, and ca-certificates for Typst installation
RUN apt-get update && apt-get install -y \
    maven \
    wget \
    xz-utils \
    ca-certificates \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Install Typst CLI
RUN wget -qO typst.tar.xz https://github.com/typst/typst/releases/download/v0.14.0/typst-x86_64-unknown-linux-musl.tar.xz && \
    tar xf typst.tar.xz --strip-components=1 -C /usr/local/bin typst-x86_64-unknown-linux-musl/typst && \
    rm typst.tar.xz && \
    chmod +x /usr/local/bin/typst && \
    typst --version

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy source code and pom.xml
COPY pom.xml .
COPY src ./src

# Build the application (will download dependencies during build)
RUN mvn clean package -DskipTests && \
    cp target/calculator-backend-1.0.0.jar app.jar

# Change ownership to spring user
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

# Optimize JVM for containers
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]