package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.services.RoomService;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody RoomDTO data, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            roomService.save(data);
            return new ResponseEntity<>(new MessageDTO("Room created"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody RoomDTO data, @PathVariable Integer id, BindingResult validations) throws Exception{
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try{
            roomService.update(data,id);
            return new ResponseEntity<>(new MessageDTO("Room created"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception{
        try{
            roomService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Room deleted"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id,
                                    @RequestParam(required = false) String lang){
        if(id != null && lang != null){
            return new ResponseEntity<>(roomService.findById(id, lang), HttpStatus.OK);
        }
        else if(lang != null){
            return new ResponseEntity<>(roomService.findByLanguage(lang), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(roomService.getAll(), HttpStatus.OK);
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
}
