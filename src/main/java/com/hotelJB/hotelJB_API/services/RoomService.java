package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    // CRUD
    void save(RoomDTO data) throws Exception;
    void update(RoomDTO data, int roomId) throws Exception;
    void delete(int roomId) throws Exception;

    // Admin
    List<Room> getAll();
    Optional<RoomResponse> findById(int roomId, String language);
    List<RoomResponse> findByLanguage(String language);

    // Cliente - solo habitaciones disponibles en fechas
    List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int maxCapacity, String language);

    // Crear y actualizar con imagen
    void saveRoomWithImage(RoomWithImageDTO dto);

    List<RoomResponse> getAllWithCategory();

    void updateRoomWithImage(Integer roomId, RoomWithImageDTO dto);

    boolean isRoomAvailable(Integer roomId, LocalDate initDate, LocalDate finishDate);

}
