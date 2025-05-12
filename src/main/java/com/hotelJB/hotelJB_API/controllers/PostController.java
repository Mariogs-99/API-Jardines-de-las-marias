package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.PostDTO;
import com.hotelJB.hotelJB_API.services.FileStorageService;
import com.hotelJB.hotelJB_API.services.PostService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAndCreateMultimedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titleEs") String titleEs,
            @RequestParam("titleEn") String titleEn,
            @RequestParam("descriptionEs") String descriptionEs,
            @RequestParam("descriptionEn") String descriptionEn,
            @RequestParam("categoryId") Integer categoryId){

        try {
            // Validar archivo vacío
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageDTO("El archivo no puede estar vacío."));
            }

            // Guardar el archivo en el servidor
            String filePath = fileStorageService.saveFile(file);

            // Crear el objeto DTO con la información de la imagen
            PostDTO postDTO = new PostDTO(titleEs,titleEn,descriptionEs,descriptionEn,filePath,categoryId);

            // Guardar la información en la base de datos
            postService.save(postDTO);

            return ResponseEntity.ok(new MessageDTO("Post subido y creado con éxito."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Error al subir el archivo o guardar la información: " + e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody PostDTO data, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            postService.save(data);
            return new ResponseEntity<>(new MessageDTO("Post created"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PostDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            postService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("Post created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            postService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Post deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) Integer categoryId,
                                    @RequestParam(required = false) String lang){
        if(id != null && lang != null){
            return new ResponseEntity<>(postService.findById(id,lang), HttpStatus.OK);
        }
        else if(categoryId != null){
            return new ResponseEntity<>(postService.findByCategory(categoryId,lang), HttpStatus.OK);
        }
        else if(lang != null){
            return new ResponseEntity<>(postService.findByLanguage(lang), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(postService.getAll(), HttpStatus.OK);
        }
    }
}
