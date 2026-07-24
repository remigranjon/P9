# --- Étape 1 : Build (Utilisation de Maven avec Java 21) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Optimisation : On copie le pom.xml d'abord pour mettre en cache les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# On copie les sources et on build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Runtime (Image légère Java 21) ---
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app

# Récupération du port depuis le docker-compose
ARG SERVICE_PORT
ENV SERVICE_PORT=${SERVICE_PORT}

# Documentation du port (pour Docker Desktop/Portainer)
EXPOSE ${SERVICE_PORT}

# Copie de l'artefact depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exécution avec interprétation du port via le shell
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${SERVICE_PORT} -jar app.jar"]