package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RoomxDetailDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxRoomDetail;

import java.util.List;
import java.util.Optional;

public interface RoomxDetailService {
    void save(RoomxDetailDTO data) throws Exception;
    void update(RoomxDetailDTO data, int roomxDetailId) throws Exception;
    void delete(int roomxDetailId) throws Exception;
    List<RoomxRoomDetail> getAll();
    Optional<RoomxRoomDetail> findById(int roomId);
}
