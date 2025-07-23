# AGENTS.md - Development Guidelines

## Build/Test/Lint Commands
- **Full build**: `docker-compose up --build` (builds all services)
- **Frontend**: `cd frontend && npm start` (dev server), `npm test` (single test run), `npm run build` (production)
- **Backend**: `cd backend && mvn spring-boot:run` (dev server), `mvn test` (all tests), `mvn test -Dtest=ClassName` (single test)
- **Single service**: `docker-compose up frontend` or `docker-compose up backend`
- **Deploy**: GitHub Actions builds and pushes to `ghcr.io/jvandelocht/website` on main branch changes

## Code Style Guidelines
### Java (Spring Boot Backend)
- **Package structure**: `uk.vandelocht.website.{controller,service,dto,exception,config}`
- **Naming**: PascalCase classes, camelCase methods/fields, UPPER_SNAKE_CASE constants
- **Annotations**: Use `@Service`, `@RestController`, `@RequestMapping`, `@Valid` for validation
- **Error handling**: Custom exceptions extending `RuntimeException`, global exception handlers
- **Validation**: Use Bean Validation annotations (`@NotNull`, `@Valid`) in DTOs

### React Frontend
- **Components**: Functional components with hooks, PascalCase filenames
- **Imports**: React first, then libraries, then local imports (components, utils)
- **Styling**: CSS classes with kebab-case, prefer CSS modules or styled-components
- **State**: Use `useState`/`useEffect` hooks, avoid class components
- **API calls**: Use axios with proper error handling and loading states

### General
- **Indentation**: 4 spaces for Java, 2 spaces for JS/HTML/CSS
- **Line length**: 120 characters max
- **Imports**: Group and sort imports, remove unused imports