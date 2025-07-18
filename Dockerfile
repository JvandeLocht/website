FROM nginx:alpine

# Copy website files
COPY index.html /usr/share/nginx/html/
COPY cv.pdf /usr/share/nginx/html/

# Optional: Add custom nginx config for better caching
RUN echo 'server { \
    listen 80; \
    location ~* \.(pdf)$ { \
        expires 30d; \
        add_header Cache-Control "public, immutable"; \
    } \
}' > /etc/nginx/conf.d/cache.conf
