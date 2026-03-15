# Stage 1: Build React frontend
FROM node:20-alpine AS react-build

WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Spring Boot backend
FROM maven:3.9.14-eclipse-temurin-21-alpine AS backend-build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Copy React build from previous stage into Spring Boot static resources
COPY --from=react-build /frontend/dist ./src/main/resources/static

# Build Spring Boot JAR (npm is NOT used here)
RUN mvn clean package -DskipTests -B -P!local-frontend

# Stage 3: Runtime image
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=backend-build /app/target/resume-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]