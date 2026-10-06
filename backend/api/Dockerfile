# ---------------------------------------------------------
# Dockerfile para Backend Spring Boot - Laboratorio PaaS DevOps
# Cumple con el contrato obligatorio: ARG PORT, ENV PORT=${PORT}, EXPOSE ${PORT}
# ---------------------------------------------------------

# Stage 1: Build del artefacto JAR
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Stage 2: Imagen final liviana de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ARG PORT=3000
ENV PORT=${PORT}

# Crear usuario sin privilegios root
RUN addgroup -S sikalmagroup && adduser -S sikalmauser -G sikalmagroup

COPY --from=builder /workspace/app/target/*.jar app.jar

RUN chown -R sikalmauser:sikalmagroup /app
USER sikalmauser

EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-local} -jar app.jar"]
