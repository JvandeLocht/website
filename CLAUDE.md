# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack personal website with integrated calculator functionality. The project consists of three main services:
- **Frontend**: React application (port 3001 in dev) serving calculator UI at `/calc`
- **Backend**: Spring Boot API (port 8081 in dev) providing REST endpoints
- **Nginx**: Static file server (port 8000 in dev) serving resume at `/`

## Development Environment

### Prerequisites
- Nix with flakes enabled
- Podman and Podman Compose

### Setup Commands
```bash
# Enter development environment
nix develop

# Start all services for development
podman-compose up

# Or start services individually:
cd frontend && npm run dev              # React dev server
cd backend && mvn spring-boot:run       # Spring Boot API
```

## Common Development Commands

### Frontend (React)
```bash
cd frontend
npm install                 # Install dependencies
npm run dev                 # Start development server (port 3000)
npm run build              # Build for production
npm test                   # Run tests
```

### Backend (Spring Boot)
```bash
cd backend
mvn clean install         # Build project
mvn spring-boot:run        # Start development server (port 8080)
mvn test                   # Run tests
mvn clean package         # Build JAR
```

### Docker Operations
```bash
podman-compose up --build  # Build and start all services
podman-compose down        # Stop all services
podman-compose logs <service>  # View service logs
```

## Architecture

### Service Communication
- Frontend uses proxy configuration in `package.json`: `"proxy": "http://backend:8080"`
- All `/api/*` requests from frontend are proxied to backend
- Backend exposes REST API at `/api/calculate` and `/api/health`

### Key Files and Structure
- `frontend/src/pages/CalculatorPage.js` - Main calculator UI component
- `backend/src/main/java/uk/vandelocht/website/controller/CalculatorController.java` - REST API endpoints
- `backend/src/main/java/uk/vandelocht/website/service/CalculatorService.java` - Business logic
- `backend/src/main/java/uk/vandelocht/website/dto/` - Request/response DTOs
- `nginx/nginx.conf` - Nginx configuration for static file serving

### Technology Stack
- **Frontend**: React 18, Axios, React Router
- **Backend**: Spring Boot 3.2.0, Java 21, Maven, Bean Validation, Spring Security
- **Infrastructure**: Docker, Nginx, Nix for development environment

### Security Features
- Input validation on both client and server side
- CORS configuration in Spring Boot
- Non-root users in Docker containers
- Rate limiting and security headers

## Development Workflow

1. Use `nix develop` to enter the development environment
2. Start services with `podman-compose up` or individually
3. Frontend available at http://localhost:3001
4. Backend API at http://localhost:8081
5. Static resume at http://localhost:8000

## API Endpoints

- `POST /api/calculate` - Calculator operations (add, subtract, multiply, divide, power, sqrt)
- `GET /api/health` - Health check endpoint

### Request Format
```json
{
  "operation": "add|subtract|multiply|divide|power|sqrt",
  "num1": 10.0,
  "num2": 5.0
}
```

### Response Format
```json
{
  "result": 15.0,
  "message": null
}
```

## Deployment

The project uses GitHub Actions for CI/CD, building separate container images:
- `ghcr.io/jvandelocht/website-frontend:latest`
- `ghcr.io/jvandelocht/website-backend:latest`
- `ghcr.io/jvandelocht/website-nginx:latest`

Production deployment uses `docker-compose.prod.yml` with pre-built images.