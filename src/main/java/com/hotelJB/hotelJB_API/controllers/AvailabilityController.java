package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.services.AvailabilitySchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilitySchedulerService availabilitySchedulerService;

    @PostMapping("/update")
    public ResponseEntity<String> actualizarDisponibilidad() {
        availabilitySchedulerService.updateRoomAvailability();
        return ResponseEntity.ok("Disponibilidad de habitaciones actualizada correctamente.");
    }
}
