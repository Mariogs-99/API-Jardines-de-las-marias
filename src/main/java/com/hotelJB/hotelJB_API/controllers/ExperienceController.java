package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ExperienceDTO;
import com.hotelJB.hotelJB_API.models.responses.ExperienceResponse;
import com.hotelJB.hotelJB_API.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<ExperienceResponse> create(@RequestBody ExperienceDTO dto) {
        return ResponseEntity.ok(experienceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> update(@PathVariable Long id, @RequestBody ExperienceDTO dto) {
        return ResponseEntity.ok(experienceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAll() {
        return ResponseEntity.ok(experienceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getById(id));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo subir la imagen"));
        }
    }

    @GetMapping("/public")
    public ResponseEntity<List<ExperienceResponse>> getPublicExperiences(
            @RequestParam(defaultValue = "es") String lang) {
        return ResponseEntity.ok(experienceService.getAllPublic(lang));
    }


}
