# AGENTS.md - Development Guidelines

## Build/Test/Lint Commands
- **Build**: `docker build -t website .` (builds Docker image with nginx)
- **Deploy**: GitHub Actions automatically builds and pushes to `ghcr.io/jvandelocht/website` on push to main
- **Local serve**: `docker run -p 8080:80 website` or serve `index.html` with any static server
- **No tests**: This is a static HTML resume site with no test framework

## Code Style Guidelines
- **HTML**: Use semantic HTML5, proper indentation (2 spaces), lowercase attributes
- **CSS**: Use CSS custom properties (`:root` variables), BEM-like naming, mobile-first responsive design
- **Formatting**: 2-space indentation, kebab-case for CSS classes, camelCase for CSS custom properties
- **Colors**: Use CSS custom properties defined in `:root` (--primary-color, --secondary-color, etc.)
- **Animations**: Prefer CSS animations over JavaScript, use `ease-out` timing functions
- **Icons**: Inline SVG with `class="icon"` and `viewBox="0 0 24 24"`
- **German content**: All user-facing text should be in German (this is a German resume)

## File Structure
- `index.html` - Main resume page with embedded CSS
- `cv.pdf` - PDF version of resume
- `Dockerfile` - nginx-alpine container setup
- `.github/workflows/build.yaml` - CI/CD pipeline

## Docker & Deployment
- Uses nginx:alpine base image
- Serves static files from `/usr/share/nginx/html/`
- PDF caching configured for 30 days
- Auto-deploys to GitHub Container Registry on main branch changes