package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.DetailRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.DetailRoom;
import com.hotelJB.hotelJB_API.models.responses.DetailRoomResponse;

import java.util.List;
import java.util.Optional;

public interface DetailRoomService {
    void save(DetailRoomDTO data) throws Exception;
    void update(DetailRoomDTO data, int detailRoomId) throws Exception;
    void delete(int detailRoomId) throws Exception;
    List<DetailRoom> getAll();
    Optional<DetailRoomResponse> findById(int detailRoomId, String language);
    List<DetailRoomResponse> findByLanguage(String language);

}
