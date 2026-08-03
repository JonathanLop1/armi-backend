# Stage 1: Compilación Multietapa con Cacheo Robusto de Dependencias
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Aprovechar la caché de capas de Docker para dependencias Maven
COPY pom.xml .
RUN mvn package -DskipTests --fail-never -B

COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime Ultraliviano en Producción (Menos de 200MB RAM)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Flags de JVM para rendimiento de bajo consumo
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC -XX:MaxMetaspaceSize=128m"

# Entrypoint robusto que evalúa variables de entorno en tiempo de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}"]
