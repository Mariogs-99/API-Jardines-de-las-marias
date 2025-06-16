package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.RestaurantDTO;
import com.hotelJB.hotelJB_API.models.responses.RestaurantResponse;
import com.hotelJB.hotelJB_API.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/highlighted")
    public ResponseEntity<List<RestaurantResponse>> getHighlighted() {
        return ResponseEntity.ok(service.getHighlighted());
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<String> uploadMenuPdf(@RequestParam("file") MultipartFile file) {
        try {
            String folderPath = System.getProperty("user.dir") + "/menu";
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = folderPath + "/" + file.getOriginalFilename();
            File dest = new File(filePath);
            file.transferTo(dest);

            String relativePath = "/menu/" + file.getOriginalFilename();
            return ResponseEntity.ok(relativePath);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo PDF.");
        }
    }

    // ✅ Crear restaurante con imagen y PDF (con soporte para inglés)
    @PostMapping("/with-files")
    public ResponseEntity<RestaurantResponse> createWithFiles(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("schedule") String schedule,
            @RequestParam("nameEn") String nameEn,
            @RequestParam("descriptionEn") String descriptionEn,
            @RequestParam("scheduleEn") String scheduleEn,
            @RequestParam("highlighted") boolean highlighted,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdf", required = false) MultipartFile pdf
    ) {
        return ResponseEntity.ok(service.createWithFiles(
                name, description, schedule,
                nameEn, descriptionEn, scheduleEn,
                highlighted, image, pdf
        ));
    }

    // ✅ Actualizar restaurante con imagen y PDF (con soporte para inglés)
    @PutMapping("/with-files/{id}")
    public ResponseEntity<RestaurantResponse> updateWithFiles(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("schedule") String schedule,
            @RequestParam("nameEn") String nameEn,
            @RequestParam("descriptionEn") String descriptionEn,
            @RequestParam("scheduleEn") String scheduleEn,
            @RequestParam("highlighted") boolean highlighted,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdf", required = false) MultipartFile pdf
    ) {
        return ResponseEntity.ok(service.updateWithFiles(
                id, name, description, schedule,
                nameEn, descriptionEn, scheduleEn,
                highlighted, image, pdf
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
