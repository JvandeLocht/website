FROM nginx:alpine

# Copy website files
COPY index.html /usr/share/nginx/html/

# Create proper nginx config
RUN echo 'server { \
    listen 80; \
    server_name _; \
    root /usr/share/nginx/html; \
    index index.html; \
    \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
    \
}' > /etc/nginx/conf.d/default.conf
