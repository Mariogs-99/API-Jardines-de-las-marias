package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.services.RoomService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody RoomDTO data, BindingResult validations) throws Exception {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            roomService.save(data);
            return new ResponseEntity<>(new MessageDTO("Room created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody RoomDTO data, @PathVariable Integer id, BindingResult validations) throws Exception {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            roomService.update(data, id);
            return new ResponseEntity<>(new MessageDTO("Room updated"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        try {
            roomService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Room deleted"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) String lang) {
        try {
            if (id != null && lang != null) {
                return new ResponseEntity<>(roomService.findById(id, lang), HttpStatus.OK);
            } else if (lang != null) {
                return new ResponseEntity<>(roomService.findByLanguage(lang), HttpStatus.OK);
            } else {
                // ⚠️ Evitar exponer entidades directamente
                return new ResponseEntity<>(roomService.findByLanguage("es"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms(@RequestParam LocalDate initDate,
                                               @RequestParam LocalDate finishDate,
                                               @RequestParam Integer maxCapacity,
                                               @RequestParam String lang) {
        try {
            List<RoomResponse> availableRooms = roomService.getAvailableRooms(initDate, finishDate, maxCapacity, lang);

            if (availableRooms.isEmpty()) {
                return new ResponseEntity<>(new MessageDTO("No hay habitaciones disponibles en este rango de fechas"), HttpStatus.OK);
            }

            return new ResponseEntity<>(availableRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/with-image", consumes = "multipart/form-data")
    public ResponseEntity<?> saveWithImage(@ModelAttribute RoomWithImageDTO dto) {
        try {
            roomService.saveRoomWithImage(dto);
            return new ResponseEntity<>(new MessageDTO("Room created with image"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error al guardar habitación con imagen"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/with-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateWithImage(@PathVariable Integer id, @ModelAttribute RoomWithImageDTO dto) {
        try {
            roomService.updateRoomWithImage(id, dto);
            return ResponseEntity.ok(new MessageDTO("Habitación actualizada correctamente"));
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error al actualizar habitación con imagen"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomResponse>> getAllRoomsWithCategory() {
        return ResponseEntity.ok(roomService.getAllWithCategory());
    }

    @GetMapping("/available/{roomId}")
    public ResponseEntity<?> isRoomAvailable(@PathVariable Integer roomId,
                                             @RequestParam LocalDate initDate,
                                             @RequestParam LocalDate finishDate) {
        try {
            boolean available = roomService.isRoomAvailable(roomId, initDate, finishDate);
            return ResponseEntity.ok().body(Collections.singletonMap("available", available));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageDTO("Error al verificar disponibilidad"));
        }
    }


}

