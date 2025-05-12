# Usar una imagen base de Java 17
FROM openjdk:17-jdk-slim

# Configurar el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar generado al contenedor
COPY build/libs/hotelJB-API-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa tu aplicación (por defecto, 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]