# Build React app first
FROM docker.io/node:20-alpine AS frontend-builder

WORKDIR /app

# Copy frontend package files and install dependencies
COPY frontend/package*.json ./
RUN npm ci --only=production && npm cache clean --force

# Copy frontend source code and build
COPY frontend/ ./
RUN npm run build

# Main nginx image
FROM docker.io/nginx:alpine

# Copy custom nginx configuration
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# Copy resume files
COPY resume.html /usr/share/nginx/html/index.html
COPY cv.pdf /usr/share/nginx/html/cv.pdf

# Copy built React app
COPY --from=frontend-builder /app/build /usr/share/nginx/html/calc

# Create directory for logs
RUN mkdir -p /var/log/nginx

# Set proper permissions
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]