package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.CategoryClientViewDTO;
import com.hotelJB.hotelJB_API.models.dtos.CategoryRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.CategoryRoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryRoomServiceImpl implements CategoryRoomService {

    @Autowired
    private CategoryRoomRepository categoryRoomRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ImgRepository imgRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void save(CategoryRoomDTO data) throws Exception {
        try {
            CategoryRoom categoryRoom = new CategoryRoom(
                    data.getNameCategoryEs(),
                    data.getNameCategoryEn(),
                    data.getDescriptionEs(),
                    data.getDescriptionEn()
            );

            categoryRoom.setMaxPeople(data.getMaxPeople());
            categoryRoom.setBedInfo(data.getBedInfo());
            categoryRoom.setRoomSize(data.getRoomSize());
            categoryRoom.setHasTv(data.getHasTv());
            categoryRoom.setHasAc(data.getHasAc());
            categoryRoom.setHasPrivateBathroom(data.getHasPrivateBathroom());

            categoryRoomRepository.save(categoryRoom);
        } catch (Exception e) {
            throw new Exception("Error al guardar categoría de habitación");
        }
    }

    @Override
    public void update(CategoryRoomDTO data, int categoryRoomId) throws Exception {
        try {
            CategoryRoom categoryRoom = categoryRoomRepository.findById(categoryRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "categoryRoom"));

            categoryRoom.setNameCategoryEs(data.getNameCategoryEs());
            categoryRoom.setNameCategoryEn(data.getNameCategoryEn());
            categoryRoom.setDescriptionEs(data.getDescriptionEs());
            categoryRoom.setDescriptionEn(data.getDescriptionEn());
            categoryRoom.setMaxPeople(data.getMaxPeople());
            categoryRoom.setBedInfo(data.getBedInfo());
            categoryRoom.setRoomSize(data.getRoomSize());
            categoryRoom.setHasTv(data.getHasTv());
            categoryRoom.setHasAc(data.getHasAc());
            categoryRoom.setHasPrivateBathroom(data.getHasPrivateBathroom());

            categoryRoomRepository.save(categoryRoom);
        } catch (Exception e) {
            throw new Exception("Error al actualizar categoría de habitación");
        }
    }

    @Override
    public void delete(int categoryRoomId) throws Exception {
        try {
            CategoryRoom categoryRoom = categoryRoomRepository.findById(categoryRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "CategoryRoom"));

            categoryRoomRepository.delete(categoryRoom);
        } catch (Exception e) {
            throw new Exception("Error delete categoryRoom");
        }
    }

    @Override
    public List<CategoryRoom> getAll() {
        return categoryRoomRepository.findAll();
    }

    @Override
    public Optional<CategoryRoomResponse> findById(int categoryRoomId, String lang) {
        return categoryRoomRepository.findById(categoryRoomId).map(value -> new CategoryRoomResponse(
                value.getCategoryRoomId(),
                "es".equals(lang) ? value.getNameCategoryEs() : value.getNameCategoryEn(),
                "es".equals(lang) ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getRoomSize(),
                value.getBedInfo(),
                null, // extraInfo si no se usa
                Boolean.TRUE.equals(value.getHasTv()),
                Boolean.TRUE.equals(value.getHasAc()),
                Boolean.TRUE.equals(value.getHasPrivateBathroom())
        ));
    }

    @Override
    public List<CategoryRoomResponse> findByLanguage(String language) {
        return categoryRoomRepository.findAll().stream().map(value -> new CategoryRoomResponse(
                value.getCategoryRoomId(),
                "es".equals(language) ? value.getNameCategoryEs() : value.getNameCategoryEn(),
                "es".equals(language) ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getRoomSize(),
                value.getBedInfo(),
                null, // extraInfo si no se usa
                Boolean.TRUE.equals(value.getHasTv()),
                Boolean.TRUE.equals(value.getHasAc()),
                Boolean.TRUE.equals(value.getHasPrivateBathroom())
        )).toList();
    }

    @Override
    public List<CategoryClientViewDTO> getCategoriesForClientView() {
        List<CategoryRoom> categories = categoryRoomRepository.findAll();

        return categories.stream().map(category -> {
            Optional<Room> cheapestRoom = roomRepository
                    .findFirstByCategoryRoom_CategoryRoomIdOrderByPriceAsc((long) category.getCategoryRoomId());

            CategoryClientViewDTO dto = new CategoryClientViewDTO();
            dto.setCategoryRoomId((long) category.getCategoryRoomId());
            dto.setNameCategoryEs(category.getNameCategoryEs());
            dto.setDescriptionEs(category.getDescriptionEs());
            dto.setMaxPeople(category.getMaxPeople());
            dto.setBedInfo(category.getBedInfo());
            dto.setRoomSize(category.getRoomSize());
            dto.setHasTv(Boolean.TRUE.equals(category.getHasTv()));
            dto.setHasAc(Boolean.TRUE.equals(category.getHasAc()));
            dto.setHasPrivateBathroom(Boolean.TRUE.equals(category.getHasPrivateBathroom()));

            cheapestRoom.ifPresent(room -> {
                dto.setMinPrice(BigDecimal.valueOf(room.getPrice()));

                Img img = room.getImg();
                if (img != null) {
                    dto.setImageUrl(baseUrl + img.getPath());
                } else {
                    dto.setImageUrl("/img/default.jpg");
                }
            });

            return dto;
        }).toList();
    }
}
