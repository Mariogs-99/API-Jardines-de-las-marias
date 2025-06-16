package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.EventDTO;
import com.hotelJB.hotelJB_API.models.dtos.EventWithImageDTO;
import com.hotelJB.hotelJB_API.models.responses.EventResponse;
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

    // ✅ Endpoint público con soporte de idioma
    @GetMapping("/public")
    public ResponseEntity<List<EventResponse>> getPublicEvents(
            @RequestParam(defaultValue = "es") String lang
    ) {
        return ResponseEntity.ok(eventService.getPublicEvents(lang));
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO dto = eventService.getById(id);
        if (!dto.isActive()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.create(eventDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody EventDTO eventDTO
    ) {
        return ResponseEntity.ok(eventService.update(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/with-image")
    public ResponseEntity<Void> createEventWithImage(@ModelAttribute EventWithImageDTO dto) {
        eventService.saveEventWithImage(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/with-image/{id}")
    public ResponseEntity<Void> updateEventWithImage(
            @PathVariable Long id,
            @ModelAttribute EventWithImageDTO dto
    ) {
        eventService.updateEventWithImage(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getAllForAdmin() {
        return ResponseEntity.ok(eventService.getAllAdmin());
    }
}
