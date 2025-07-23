# Multi-stage build for production deployment
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

FROM openjdk:21-jdk-slim AS backend-build
WORKDIR /app/backend
COPY backend/pom.xml ./
COPY backend/src ./src
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests && \
    apt-get remove -y maven && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*

FROM nginx:alpine
# Copy nginx configuration
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# Copy resume files
COPY resume.html /usr/share/nginx/html/index.html
COPY cv.pdf /usr/share/nginx/html/cv.pdf

# Copy built frontend
COPY --from=frontend-build /app/frontend/build /usr/share/nginx/html/calc

# Copy backend jar (for potential future use)
COPY --from=backend-build /app/backend/target/calculator-backend-1.0.0.jar /app/backend.jar

# Install Java for running backend alongside nginx
RUN apk add --no-cache openjdk21-jre

# Create startup script
RUN echo '#!/bin/sh' > /start.sh && \
    echo 'java -jar /app/backend.jar &' >> /start.sh && \
    echo 'nginx -g "daemon off;"' >> /start.sh && \
    chmod +x /start.sh

EXPOSE 80 8080

CMD ["/start.sh"]
