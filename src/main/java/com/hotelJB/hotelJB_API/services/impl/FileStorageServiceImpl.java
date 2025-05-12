package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path storagePath;

    public FileStorageServiceImpl(@Value("${MULTIMEDIA_STORAGE_PATH}") String storageDir) {
        this.storagePath = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storagePath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        try {
            // Validar archivo vacío
            if (file.isEmpty()) {
                throw new RuntimeException("No se puede guardar un archivo vacío.");
            }

            // Limpiar el nombre del archivo
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Validar nombre de archivo
            if (fileName.contains("..")) {
                throw new RuntimeException("El nombre del archivo contiene caracteres no permitidos.");
            }

            // Guardar el archivo en el directorio de almacenamiento
            Path targetLocation = storagePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo. Por favor, intenta nuevamente.", e);
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            Path filePath = storagePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("No se pudo leer el archivo: " + fileName);
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la imagen", e);
        }
    }

    @Override
    public Path getStoragePath() {
        return this.storagePath;
    }
}
