package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.MessageDTO;
import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.services.ReservationService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody ReservationDTO data, BindingResult validations) throws Exception {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            reservationService.save(data);
            return new ResponseEntity<>(new MessageDTO("Reservation created"), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ReservationDTO data, @PathVariable Integer id, BindingResult validations) throws Exception {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        try {
            reservationService.update(data, id);
            return new ResponseEntity<>(new MessageDTO("Reservation updated"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        try {
            reservationService.delete(id);
            return new ResponseEntity<>(new MessageDTO("Reservation deleted"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer id) {
        if (id != null) {
            return new ResponseEntity<>(reservationService.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(reservationService.getAllResponses(), HttpStatus.OK);
        }
    }

    @GetMapping("/unavailable/hotel")
    public ResponseEntity<?> getFullyBookedDatesForHotel() {
        try {
            List<Map<String, LocalDate>> unavailableDates = reservationService.getFullyBookedDatesForHotel();

            if (unavailableDates.isEmpty()) {
                return new ResponseEntity<>(new MessageDTO("Todas las fechas están disponibles"), HttpStatus.OK);
            }

            return new ResponseEntity<>(unavailableDates, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("initDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initDate,
            @RequestParam("finishDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishDate,
            @RequestParam("cantPeople") int cantPeople
    ) {
        List<RoomResponse> availableRooms = reservationService.getAvailableRooms(initDate, finishDate, cantPeople);
        return ResponseEntity.ok(availableRooms);
    }

    @PutMapping("/{id}/assign-rooms")
    public ResponseEntity<?> assignRoomNumbers(
            @PathVariable Integer id,
            @RequestBody List<ReservationRoomDTO> assignments
    ) {
        try {
            reservationService.assignRoomNumbers(id, assignments);
            return new ResponseEntity<>(new MessageDTO("Números de habitación asignados correctamente"), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Error al asignar los números de habitación"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 🔹 NUEVO: buscar reserva por número de habitación
    @GetMapping("/by-room")
    public ResponseEntity<?> getByRoomNumber(@RequestParam String roomNumber) {
        try {
            ReservationResponse response = reservationService.getByRoomNumber(roomNumber);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
