package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomxImgDTO;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.repositories.RoomxImgRepository;
import com.hotelJB.hotelJB_API.services.RoomxImgService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@RestController
@RequestMapping("/api/room-img")
public class RoomxImgController {
    @Autowired
    private RoomxImgService roomxImgService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Autowired
    private ImgRepository imgRepository;

    @Autowired
    private RoomxImgRepository roomxImgRepository;


    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody RoomxImgDTO data, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            roomxImgService.save(data);
            return new ResponseEntity<>(new MessageDTO("RoomxImg created"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody RoomxImgDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            roomxImgService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("RoomxImg created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            roomxImgService.delete(id);
            return new ResponseEntity<>(new MessageDTO("RoomxImg deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) Integer roomId){
        if(id != null){
            return new ResponseEntity<>(roomxImgService.findById(id), HttpStatus.OK);
        }
        else if(roomId != null){
            return new ResponseEntity<>(roomxImgService.findByRoomId(roomId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(roomxImgService.getAll(), HttpStatus.OK);
        }
    }


    //?-------------------------Subir imagenes--------------

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAndAssignImage(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("roomId") int roomId) {
        try {
            // 1. Generar nombre único
            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 2. Ruta donde se guardará físicamente
            Path path = Paths.get("uploads", uniqueName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 3. Guardar en tabla img
            Img img = new Img();
            img.setNameImg(file.getOriginalFilename());
            img.setPath("uploads/" + uniqueName); // Puedes guardar solo la ruta relativa
            imgRepository.save(img); // asegúrate de inyectarlo

            // 4. Asociar a la habitación en roomximg
            RoomxImg relation = new RoomxImg(roomId, img.getImgId());
            roomxImgRepository.save(relation);

            return ResponseEntity.ok(new MessageDTO("Imagen subida y asociada a la habitación correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Error al subir la imagen: " + e.getMessage()));
        }
    }

}
