package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    void save(RoomDTO data) throws Exception;
    void update(RoomDTO data, int roomId) throws Exception;
    void delete(int roomId) throws Exception;
    List<Room> getAll();
    Optional<RoomResponse> findById(int roomId, String language);
    List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int maxCapacity, String language);
    List<RoomResponse> findByLanguage(String language);
    void saveRoomWithImage(RoomWithImageDTO dto);
    void updateRoomWithImage(Integer roomId, RoomWithImageDTO dto);


}
