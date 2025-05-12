package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RoomxRoomDetailDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxRoomDetail;

import java.util.List;
import java.util.Optional;

public interface DetailRoomxRoomService {
    void save(RoomxRoomDetailDTO data) throws Exception;
    void update(RoomxRoomDetailDTO data, int detailRoomId) throws Exception;
    void delete(int detailRoomId) throws Exception;
    List<RoomxRoomDetail> getAll();
    Optional<RoomxRoomDetail> findById(int detailRoomId);
}
