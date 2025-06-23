# Etapa 1: Build con Gradle Wrapper y Java 21
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copiamos todos los archivos del proyecto
COPY . .

# Aseguramos que el wrapper tiene permisos de ejecución
RUN chmod +x ./gradlew

# Compilamos el proyecto, omitiendo tests para producción más rápida
RUN ./gradlew build -x test

# Etapa 2: Imagen final con JDK 21 para ejecutar la app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el .jar generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
