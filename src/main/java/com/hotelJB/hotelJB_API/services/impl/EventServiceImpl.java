package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.EventDTO;
import com.hotelJB.hotelJB_API.models.dtos.EventWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.Event;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.responses.EventResponse;
import com.hotelJB.hotelJB_API.repositories.EventRepository;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ImgRepository imgRepository;

    @Override
    public List<EventDTO> getAll() {
        return eventRepository.findByActiveTrueOrderByEventDateAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getById(Long id) {
        return eventRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }

    @Override
    public EventDTO create(EventDTO dto) {
        Event event = toEntity(dto);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        return toDTO(eventRepository.save(event));
    }

    @Override
    public EventDTO update(Long id, EventDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        event.setTitleEs(dto.getTitleEs());
        event.setTitleEn(dto.getTitleEn());
        event.setDescriptionEs(dto.getDescriptionEs());
        event.setDescriptionEn(dto.getDescriptionEn());
        event.setEventDate(dto.getEventDate());
        event.setCapacity(dto.getCapacity());
        event.setPrice(dto.getPrice());
        event.setActive(dto.isActive());
        event.setUpdatedAt(LocalDateTime.now());

        return toDTO(eventRepository.save(event));
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitleEs(event.getTitleEs());
        dto.setTitleEn(event.getTitleEn());
        dto.setDescriptionEs(event.getDescriptionEs());
        dto.setDescriptionEn(event.getDescriptionEn());
        dto.setEventDate(event.getEventDate());
        dto.setCapacity(event.getCapacity());
        dto.setPrice(event.getPrice());
        dto.setActive(event.isActive());

        if (event.getImg() != null) {
            dto.setImageUrl(event.getImg().getPath());
        }

        return dto;
    }

    private Event toEntity(EventDTO dto) {
        Event event = new Event();
        event.setTitleEs(dto.getTitleEs());
        event.setTitleEn(dto.getTitleEn());
        event.setDescriptionEs(dto.getDescriptionEs());
        event.setDescriptionEn(dto.getDescriptionEn());
        event.setEventDate(dto.getEventDate());
        event.setCapacity(dto.getCapacity());
        event.setPrice(dto.getPrice());
        event.setActive(dto.isActive());
        return event;
    }

    @Override
    public void saveEventWithImage(EventWithImageDTO dto) {
        try {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = dto.getImage().getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String absolutePath = uploadDir + fileName;
            String relativePath = "uploads/" + fileName;

            File file = new File(absolutePath);
            dto.getImage().transferTo(file);

            Img img = new Img(fileName, relativePath);
            imgRepository.save(img);

            Event event = new Event();
            event.setTitleEs(dto.getTitleEs());
            event.setTitleEn(dto.getTitleEn());
            event.setDescriptionEs(dto.getDescriptionEs());
            event.setDescriptionEn(dto.getDescriptionEn());
            event.setEventDate(dto.getEventDate());
            event.setCapacity(dto.getCapacity());
            event.setPrice(dto.getPrice());
            event.setImg(img);
            event.setActive(dto.isActive());
            event.setCreatedAt(LocalDateTime.now());
            event.setUpdatedAt(LocalDateTime.now());

            eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar evento con imagen", e);
        }
    }

    @Override
    public void updateEventWithImage(Long eventId, EventWithImageDTO dto) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            event.setTitleEs(dto.getTitleEs());
            event.setTitleEn(dto.getTitleEn());
            event.setDescriptionEs(dto.getDescriptionEs());
            event.setDescriptionEn(dto.getDescriptionEn());
            event.setEventDate(dto.getEventDate());
            event.setCapacity(dto.getCapacity());
            event.setPrice(dto.getPrice());
            event.setActive(dto.isActive());
            event.setUpdatedAt(LocalDateTime.now());

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = dto.getImage().getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                String absolutePath = uploadDir + fileName;
                String relativePath = "uploads/" + fileName;

                File file = new File(absolutePath);
                dto.getImage().transferTo(file);

                Img img = new Img(fileName, relativePath);
                imgRepository.save(img);

                event.setImg(img);
            }

            eventRepository.save(event);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar evento con imagen", e);
        }
    }

    @Override
    public List<EventResponse> getPublicEvents(String lang) {
        return eventRepository.findByActiveTrueOrderByEventDateAsc()
                .stream()
                .map(event -> new EventResponse(event, lang))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getAllAdmin() {
        return eventRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
