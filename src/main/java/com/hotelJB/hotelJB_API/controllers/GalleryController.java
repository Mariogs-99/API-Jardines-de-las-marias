package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.services.FileStorageService;
import com.hotelJB.hotelJB_API.services.GalleryService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {
    @Autowired
    private GalleryService galleryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAndCreateMultimedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("categoryId") Integer categoryId) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageDTO("El archivo no puede estar vacío."));
            }

            // Guarda la imagen y obtén solo el nombre del archivo (no la ruta completa)
            String fileName = fileStorageService.saveFile(file); // ✅ debe devolver solo "imagen.jpg"

            // Creamos DTO con solo el nombre del archivo como "path"
            ImgDTO imgDTO = new ImgDTO(name, fileName, categoryId);

            galleryService.save(imgDTO);

            return ResponseEntity.ok(new MessageDTO("Archivo subido y multimedia creado con éxito."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Error al subir el archivo o guardar la información: " + e.getMessage()));
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFileById(@PathVariable int id) {
        try {
            // Obtener el archivo como un recurso desde la BD
            Resource resource = galleryService.getFileAsResourceById(id);

            // Validar si el archivo existe
            if (resource == null || !resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageDTO("El archivo no existe o no está disponible."));
            }

            // Determinar el tipo de contenido
            String contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Configurar la respuesta con el archivo
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Error al procesar la solicitud de descarga: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ImgDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            galleryService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("Gallery created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            galleryService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Gallery deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) Integer categoryId){
        if(id != null){
            return new ResponseEntity<>(galleryService.findById(id), HttpStatus.OK);
        }else if(categoryId != null){
            return new ResponseEntity<>(galleryService.findByCategory(categoryId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(galleryService.getAll(), HttpStatus.OK);
        }
    }
}
