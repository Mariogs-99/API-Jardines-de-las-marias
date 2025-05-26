package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.GalleryDTO;
import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.entities.Gallery;
import com.hotelJB.hotelJB_API.models.responses.GalleryResponse;
import com.hotelJB.hotelJB_API.repositories.GalleryRepository;
import com.hotelJB.hotelJB_API.services.FileStorageService;
import com.hotelJB.hotelJB_API.services.GalleryService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin("*")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryRepository galleryRepository;

    @GetMapping
    public ResponseEntity<List<GalleryResponse>> getAll() {
        return ResponseEntity.ok(galleryService.getAll());
    }

    @GetMapping("/public")
    public ResponseEntity<List<GalleryResponse>> getPublicGallery() {
        return ResponseEntity.ok(galleryService.getPublicGallery());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GalleryResponse> update(@PathVariable Long id, @RequestBody GalleryDTO dto) {
        return ResponseEntity.ok(galleryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        galleryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryResponse> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") GalleryDTO dto) throws IOException {

        // Ruta absoluta a la carpeta /uploads
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Files.createDirectories(Paths.get(uploadDir)); // crea la carpeta si no existe

        // Nombre único para el archivo
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);

        // Escribir archivo en disco
        Files.write(filePath, file.getBytes());

        // Llamar al servicio para guardar en base de datos
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(galleryService.save(dto, "/uploads/" + filename));
    }


}
