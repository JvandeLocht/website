# Full-Stack Calculator Application Architecture

## Overview

This document explains how your full-stack calculator application works, including Docker container communication, data flow, and component responsibilities. The application consists of three main services working together to provide a technical calculator with a web interface.

## System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Nginx       │    │   React App     │    │  Spring Boot    │
│   (Port 8000)   │    │  (Port 3001)    │    │   (Port 8081)   │
│                 │    │                 │    │                 │
│ Static Resume   │    │ Calculator UI   │    │ REST API        │
│ HTML/CSS/PDF    │    │ User Interface  │    │ Business Logic  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │                         │
                              │    HTTP Requests        │
                              │ /api/calculate          │
                              └─────────────────────────┘
```

## Components Breakdown

### 1. Frontend - React Application (Port 3001)

**Purpose**: Provides the user interface for the calculator

**Key Files**:
- `frontend/src/App.js` - Main application component with routing
- `frontend/src/pages/CalculatorPage.js` - Calculator interface and logic
- `frontend/src/components/Header.js` - Navigation header
- `frontend/package.json` - Dependencies and proxy configuration

**What it does**:
- Renders a web form for calculator operations (add, subtract, multiply, divide, power, square root)
- Validates user input on the client side before sending to backend
- Makes HTTP requests to the backend API
- Displays results and error messages to users
- Handles loading states and user interactions

**Key Technologies**:
- React 18 with functional components and hooks
- Axios for HTTP requests
- React Router for navigation
- CSS for styling

### 2. Backend - Spring Boot API (Port 8081)

**Purpose**: Handles calculator business logic and provides REST API

**Key Files**:
- `backend/src/main/java/uk/vandelocht/website/CalculatorApplication.java` - Main Spring Boot application
- `backend/src/main/java/uk/vandelocht/website/controller/CalculatorController.java` - REST API endpoints
- `backend/src/main/java/uk/vandelocht/website/service/CalculatorService.java` - Business logic
- `backend/src/main/java/uk/vandelocht/website/dto/` - Data transfer objects
- `backend/src/main/resources/application.yml` - Configuration

**What it does**:
- Exposes REST API endpoints (`/api/calculate`, `/api/health`)
- Validates calculation requests
- Performs mathematical operations
- Returns JSON responses with results or error messages
- Handles security and CORS configuration

**Key Technologies**:
- Spring Boot 3.2.0 with Java 21
- Spring Web for REST API
- Spring Security for security configuration
- Bean Validation for input validation
- Spring Actuator for health checks

### 3. Nginx - Static File Server (Port 8000)

**Purpose**: Serves static resume website

**Key Files**:
- `nginx/nginx.conf` - Nginx configuration
- `resume.html` - Static HTML resume
- `cv.pdf` - PDF version of resume

**What it does**:
- Serves static HTML resume at the root URL
- Provides PDF download functionality
- Acts as a reverse proxy (if configured)

## Docker Container Communication

### Container Network

Docker Compose creates a default network where containers can communicate using service names as hostnames:

```
Docker Network: website_default
├── website-frontend-1  (hostname: frontend)
├── website-backend-1   (hostname: backend)  
└── website-nginx-1     (hostname: nginx)
```

### Communication Flow

1. **Frontend ↔ Backend Communication**:
   ```
   Frontend Container (frontend:3000)
        ↓ HTTP Proxy
   Backend Container (backend:8080)
   ```
   - Frontend uses proxy configuration in `package.json`: `"proxy": "http://backend:8080"`
   - All `/api/*` requests are automatically forwarded to the backend
   - Backend responds with JSON data

2. **External Access**:
   ```
   Browser → Host Machine Ports → Docker Containers
   
   localhost:3001 → frontend:3000 (React Dev Server)
   localhost:8081 → backend:8080  (Spring Boot API)
   localhost:8000 → nginx:80      (Static Resume)
   ```

## Data Flow Diagram

### Calculator Operation Flow

```
1. User Input
   ┌─────────────────┐
   │ User enters:    │
   │ - Operation     │
   │ - Number 1      │
   │ - Number 2      │
   └─────────────────┘
           │
           ▼
2. Client-Side Validation
   ┌─────────────────┐
   │ React validates:│
   │ - Required fields│
   │ - Number format │
   │ - Division by 0 │
   │ - Negative sqrt │
   └─────────────────┘
           │
           ▼
3. HTTP Request
   ┌─────────────────┐
   │ POST /api/calc  │
   │ {               │
   │   operation: "add"│
   │   num1: 5       │
   │   num2: 3       │
   │ }               │
   └─────────────────┘
           │
           ▼
4. Backend Processing
   ┌─────────────────┐
   │ Spring Boot:    │
   │ - Validates DTO │
   │ - Calls Service │
   │ - Performs calc │
   │ - Returns JSON  │
   └─────────────────┘
           │
           ▼
5. Response
   ┌─────────────────┐
   │ {               │
   │   result: 8.0   │
   │   message: null │
   │ }               │
   └─────────────────┘
           │
           ▼
6. UI Update
   ┌─────────────────┐
   │ React displays: │
   │ "Result: 8.0"   │
   └─────────────────┘
```

## Request/Response Examples

### Successful Calculation
```bash
# Request
curl -X POST http://localhost:3001/api/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation":"add","num1":10,"num2":5}'

# Response
{
  "result": 15.0,
  "message": null
}
```

### Error Handling
```bash
# Request (Division by zero)
curl -X POST http://localhost:3001/api/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation":"divide","num1":10,"num2":0}'

# Response
{
  "result": null,
  "message": "Division by zero is not allowed"
}
```

## File Structure and Responsibilities

```
website/
├── frontend/                    # React Application
│   ├── src/
│   │   ├── components/
│   │   │   └── Header.js       # Navigation component
│   │   ├── pages/
│   │   │   └── CalculatorPage.js # Main calculator interface
│   │   ├── App.js              # Root component with routing
│   │   └── index.js            # React entry point
│   ├── package.json            # Dependencies & proxy config
│   └── Dockerfile              # Frontend container build
│
├── backend/                     # Spring Boot API
│   ├── src/main/java/uk/vandelocht/website/
│   │   ├── controller/
│   │   │   └── CalculatorController.java  # REST endpoints
│   │   ├── service/
│   │   │   └── CalculatorService.java     # Business logic
│   │   ├── dto/
│   │   │   ├── CalculationRequest.java    # Input DTO
│   │   │   └── CalculationResponse.java   # Output DTO
│   │   ├── exception/
│   │   │   └── CalculationException.java  # Custom exceptions
│   │   └── CalculatorApplication.java     # Main Spring Boot app
│   ├── src/main/resources/
│   │   └── application.yml     # Spring configuration
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile              # Backend container build
│
├── nginx/                       # Static File Server
│   ├── nginx.conf              # Nginx configuration
│   └── Dockerfile              # Nginx container build
│
├── docker-compose.yml          # Multi-container orchestration
├── resume.html                 # Static resume page
└── cv.pdf                      # PDF resume
```

## Docker Configuration Details

### Frontend Container
```dockerfile
FROM node:20-alpine
# Creates non-root user 'reactuser' for security
# Installs npm dependencies
# Runs React development server on port 3000
# Proxies API calls to backend container
```

### Backend Container
```dockerfile
FROM openjdk:21-jdk-slim
# Creates non-root user 'spring' for security
# Installs Maven and builds Spring Boot JAR
# Runs with JVM optimizations for containers
# Exposes port 8080 for API
```

### Nginx Container
```dockerfile
FROM nginx:alpine
# Copies static HTML and PDF files
# Configures nginx to serve static content
# Exposes port 80
```

## Deployment Strategies

### Development Environment
- **File**: `docker-compose.yml`
- **Purpose**: Local development with hot reloading
- **Containers**: 3 separate containers for easy debugging
- **Volumes**: Source code mounted for live updates

### Production Environment
- **File**: `docker-compose.prod.yml`
- **Purpose**: Production deployment with built images
- **Images**: Pre-built images from GitHub Container Registry
- **Features**: Optimized for performance and security

### CI/CD Pipeline
The GitHub Actions workflow (`.github/workflows/build.yaml`) builds three separate images:
- `ghcr.io/jvandelocht/website-frontend:latest`
- `ghcr.io/jvandelocht/website-backend:latest`
- `ghcr.io/jvandelocht/website-nginx:latest`

This allows for:
- **Independent scaling** of each service
- **Separate deployment** of components
- **Better resource utilization**
- **Easier maintenance and updates**

## Environment Variables and Configuration

### Frontend Environment
- `REACT_APP_API_URL`: API base URL (set in docker-compose.yml)
- `ESLINT_NO_DEV_ERRORS`: Disables ESLint errors in development
- `DISABLE_ESLINT_PLUGIN`: Disables ESLint plugin for containers

### Backend Environment
- `SPRING_PROFILES_ACTIVE`: Set to "dev" for development configuration
- JVM Options: `-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0`

## Security Features

1. **Non-root Users**: All containers run with dedicated non-root users
2. **Input Validation**: Both client-side and server-side validation
3. **CORS Configuration**: Controlled cross-origin resource sharing
4. **Error Handling**: Secure error messages without sensitive information
5. **Container Isolation**: Each service runs in its own container

## Performance Optimizations

1. **Frontend**:
   - React.memo for preventing unnecessary re-renders
   - useCallback/useMemo hooks for expensive operations
   - Client-side validation to reduce server requests

2. **Backend**:
   - JVM container optimizations
   - Efficient validation logic
   - Reduced logging in production

3. **Docker**:
   - Layer caching for faster builds
   - Multi-stage builds (where applicable)
   - Optimized base images

## Development Workflow

1. **Start all services**: `podman compose up --build`
2. **Access applications**:
   - Calculator: http://localhost:3001
   - API: http://localhost:8081
   - Resume: http://localhost:8000
3. **Make changes**: Code changes trigger automatic rebuilds
4. **Test**: Use curl or browser to test functionality
5. **Debug**: Check logs with `podman compose logs [service]`

## Troubleshooting Common Issues

### Frontend can't reach backend
- Check proxy configuration in `package.json`
- Verify backend container is running: `podman compose ps`
- Check network connectivity: `podman compose logs frontend`

### Backend startup failures
- Check Java version compatibility
- Verify Maven dependencies: `podman compose logs backend`
- Ensure no port conflicts

### Container permission issues
- Verify non-root users are properly configured
- Check file ownership in containers
- Review Docker volume mounts

This architecture provides a scalable, secure, and maintainable full-stack application with clear separation of concerns and robust error handling.