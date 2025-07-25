# Development Dockerfile for React app
FROM docker.io/node:20-alpine

# Create non-root user
RUN addgroup -g 1001 -S nodejs && \
    adduser -S reactuser -u 1001

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci && npm cache clean --force

# Copy source code
COPY --chown=reactuser:nodejs . .

USER reactuser

EXPOSE 3000

# Disable ESLint cache to avoid permission issues
ENV ESLINT_NO_DEV_ERRORS=true
ENV DISABLE_ESLINT_PLUGIN=true

CMD ["npm", "start"]