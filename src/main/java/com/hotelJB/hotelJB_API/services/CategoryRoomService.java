package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.CategoryClientViewDTO;
import com.hotelJB.hotelJB_API.models.dtos.CategoryRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryRoomService {
    void save(CategoryRoomDTO data) throws Exception;
    void update(CategoryRoomDTO data, int categoryId) throws Exception;
    void delete(int categoryId) throws Exception;
    List<CategoryRoom> getAll();
    Optional<CategoryRoomResponse> findById(int categoryId, String language);
    List<CategoryRoomResponse> findByLanguage(String language);
    List<CategoryClientViewDTO> getCategoriesForClientView();

}
