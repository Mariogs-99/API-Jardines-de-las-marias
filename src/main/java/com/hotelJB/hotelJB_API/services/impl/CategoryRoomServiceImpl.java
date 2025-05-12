package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.CategoryRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.services.CategoryRoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryRoomServiceImpl implements CategoryRoomService {
    @Autowired
    private CategoryRoomRepository categoryRoomRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(CategoryRoomDTO data) throws Exception {
        try{
            CategoryRoom categoryRoom = new CategoryRoom(data.getNameCategoryEs(), data.getNameCategoryEn(),
                    data.getDescriptionEs(), data.getDescriptionEn());
            categoryRoomRepository.save(categoryRoom);
        }catch (Exception e){
            throw new Exception("Error save Category Room");
        }
    }

    @Override
    public void update(CategoryRoomDTO data, int categoryRoomId) throws Exception {
        try{
            CategoryRoom categoryRoom = categoryRoomRepository.findById(categoryRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "categoyrRoom"));

            categoryRoom.setNameCategoryEs(data.getNameCategoryEs());
            categoryRoom.setNameCategoryEn(data.getNameCategoryEn());
            categoryRoom.setDescriptionEs(data.getDescriptionEs());
            categoryRoom.setDescriptionEs(data.getDescriptionEn());
            categoryRoomRepository.save(categoryRoom);
        }catch (Exception e){
            throw new Exception("Error update Detail Room");
        }
    }

    @Override
    public void delete(int categoryRoomId) throws Exception {
        try{
            CategoryRoom categoryRoom = categoryRoomRepository.findById(categoryRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "CategoryRoom"));

            categoryRoomRepository.delete(categoryRoom);
        }catch (Exception e){
            throw new Exception("Error delete categoryRoom");
        }
    }

    @Override
    public List<CategoryRoom> getAll() {
        return categoryRoomRepository.findAll();
    }

    @Override
    public Optional<CategoryRoomResponse> findById(int categoryRoomId, String lang) {
        Optional<CategoryRoom> categoryRoom = categoryRoomRepository.findById(categoryRoomId);

        return categoryRoom.map(value -> new CategoryRoomResponse(
                value.getCategoryRoomId(),
                "es".equals(lang) ? value.getNameCategoryEs() : value.getNameCategoryEn(),
                "es".equals(lang) ? value.getDescriptionEs() : value.getDescriptionEn()
        ));
    }

    @Override
    public List<CategoryRoomResponse> findByLanguage(String language) {
        List<CategoryRoom> categoryRooms = categoryRoomRepository.findAll();

        return categoryRooms.stream().map(value -> new CategoryRoomResponse(
                value.getCategoryRoomId(),
                "es".equals(language) ? value.getNameCategoryEs() : value.getNameCategoryEn(),
                "es".equals(language) ? value.getDescriptionEs() : value.getDescriptionEn()
        )).toList();
    }
}
