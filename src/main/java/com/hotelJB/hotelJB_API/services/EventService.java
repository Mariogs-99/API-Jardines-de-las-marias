package com.hotelJB.hotelJB_API.services;
import com.hotelJB.hotelJB_API.models.dtos.EventDTO;
import com.hotelJB.hotelJB_API.models.dtos.EventWithImageDTO;

import java.util.List;

public interface EventService {
    List<EventDTO> getAll();
    EventDTO getById(Long id);
    EventDTO create(EventDTO dto);
    EventDTO update(Long id, EventDTO dto);
    void delete(Long id);
    void updateEventWithImage(Long id, EventWithImageDTO dto);
    void saveEventWithImage(EventWithImageDTO dto);
    List<EventDTO> getAllAdmin();
}


