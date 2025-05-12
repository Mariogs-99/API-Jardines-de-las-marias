package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.RoomxImgDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxImg;

import java.util.List;
import java.util.Optional;

public interface RoomxImgService {
    void save(RoomxImgDTO data) throws Exception;
    void update(RoomxImgDTO data, int roomxImgId) throws Exception;
    void delete(int roomxImgId) throws Exception;
    List<RoomxImg> getAll();
    Optional<RoomxImg> findById(int roomId);
    List<RoomxImg> findByRoomId(int roomId);
}
