{
  description = "Website with React calculator and Spring Boot backend";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
    }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = nixpkgs.legacyPackages.${system};

        # Java version for Spring Boot
        jdk = pkgs.openjdk21;

        # Node.js version for React
        nodejs = pkgs.nodejs_20;

        # Maven for Spring Boot builds
        maven = pkgs.maven;

      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            # Java development
            jdk
            maven

            # Node.js development
            nodejs
            nodePackages.npm
            nodePackages.yarn

            # Build tools
            podman
            podman-compose

            # Development tools
            git
            curl
            jq

            # Nginx for local testing
            nginx
          ];

          shellHook = ''
            echo "🚀 Development environment ready!"
            echo "Java version: $(java -version 2>&1 | head -n 1)"
            echo "Node.js version: $(node --version)"
            echo "Maven version: $(mvn --version | head -n 1)"
            echo ""
            echo "Available commands:"
            echo "  npm run dev     - Start React development server"
            echo "  mvn spring-boot:run - Start Spring Boot backend"
            echo "  podman compose up - Start all services"
            echo ""

            # Set JAVA_HOME for Maven
            export JAVA_HOME="${jdk}"

            # Add node_modules/.bin to PATH for npm scripts
            export PATH="$PWD/frontend/node_modules/.bin:$PATH"
          '';
        };

        # Package for building the application
        packages.default = pkgs.stdenv.mkDerivation {
          pname = "website";
          version = "1.0.0";

          src = ./.;

          buildInputs = [
            jdk
            nodejs
            maven
          ];

          buildPhase = ''
            # Build React frontend
            cd frontend
            npm ci
            npm run build
            cd ..

            # Build Spring Boot backend
            cd backend
            mvn clean package -DskipTests
            cd ..
          '';

          installPhase = ''
            mkdir -p $out
            cp -r frontend/dist $out/frontend
            cp backend/target/*.jar $out/backend.jar
            cp -r nginx $out/
            cp docker-compose.yml $out/
          '';
        };
      }
    );
}
