package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.EventDTO;
import com.hotelJB.hotelJB_API.models.dtos.EventWithImageDTO;
import com.hotelJB.hotelJB_API.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    // Obtener todos los eventos activos
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    // Obtener un evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    // Crear evento sin imagen (opcional)
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.create(eventDTO));
    }

    // Actualizar evento sin imagen (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody EventDTO eventDTO
    ) {
        return ResponseEntity.ok(eventService.update(id, eventDTO));
    }

    // Eliminar evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Crear evento con imagen
    @PostMapping("/with-image")
    public ResponseEntity<Void> createEventWithImage(@ModelAttribute EventWithImageDTO dto) {
        eventService.saveEventWithImage(dto);
        return ResponseEntity.ok().build();
    }

    // Actualizar evento con imagen
    @PutMapping("/with-image/{id}")
    public ResponseEntity<Void> updateEventWithImage(
            @PathVariable Long id,
            @ModelAttribute EventWithImageDTO dto
    ) {
        eventService.updateEventWithImage(id, dto);
        return ResponseEntity.ok().build();
    }
}
