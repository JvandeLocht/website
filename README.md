# Personal Website with Calculator

This repository contains Jan van de Locht's personal website with an integrated technical calculator.

## Architecture

- **Resume**: Static HTML page served at the root (`/`)
- **Calculator**: React frontend with Spring Boot backend served at `/calc`
- **Infrastructure**: Nginx reverse proxy with Docker deployment

## Development Setup

### Prerequisites

- Nix with flakes enabled
- Podman and Podman Compose

### Getting Started

1. Enter the development environment:
```bash
nix develop
```

2. Start all services for development:
```bash
podman-compose up
```

3. Or start services individually:
```bash
# Frontend (React)
cd frontend && npm run dev

# Backend (Spring Boot)
cd backend && mvn spring-boot:run

# Nginx (for testing routing)
podman run -p 8080:80 -v $(pwd)/nginx/nginx.conf:/etc/nginx/nginx.conf nginx:alpine
```

## Services

### Frontend (React)
- **Port**: 3000 (development), served via nginx in production
- **Path**: `/calc`
- **Features**: Technical calculator with multiple operations

### Backend (Spring Boot)
- **Port**: 8080
- **API Endpoints**: `/api/calculate`, `/api/health`
- **Security**: Input validation, rate limiting, CORS configured

### Nginx
- **Port**: 80
- **Routes**:
  - `/` → Resume (static HTML)
  - `/calc` → React calculator app
  - `/api/` → Spring Boot backend
  - `/cv.pdf` → PDF download

## Security Features

- Input validation on all calculator operations
- Rate limiting on API endpoints
- CORS properly configured
- Security headers (XSS protection, content type sniffing prevention)
- No sensitive data exposure

## Deployment

The application is automatically built and deployed via GitHub Actions to GitHub Container Registry.

### Manual Build
```bash
podman build -t website .
podman run -p 80:80 website
```

### Access Points
- Resume: `http://localhost/`
- Calculator: `http://localhost/calc`
- API Health: `http://localhost/api/health`

## Calculator Operations

- Addition (+)
- Subtraction (-)
- Multiplication (×)
- Division (÷)
- Power (^)
- Square Root (√)

All operations include proper error handling and input validation.