package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.CategoryClientViewDTO;
import com.hotelJB.hotelJB_API.models.dtos.CategoryRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Category;
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

    @Override
    public void save(CategoryRoomDTO data) throws Exception {
        try {
            CategoryRoom categoryRoom = new CategoryRoom(
                    data.getNameCategoryEs(),
                    data.getNameCategoryEn(),
                    data.getDescriptionEs(),
                    data.getDescriptionEn()
            );

            // Nuevos campos
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

            // Nuevos campos
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



    //?--------Categorias con sus precios

    public List<CategoryClientViewDTO> getCategoriesForClientView() {
        List<CategoryRoom> categories = categoryRoomRepository.findAll();

        return categories.stream().map(category -> {
            // Obtener la habitación con menor precio de esta categoría
            Optional<Room> cheapestRoom = roomRepository
                    .findFirstByCategoryRoom_CategoryRoomIdOrderByPriceAsc(Long.valueOf(category.getCategoryRoomId()));

            CategoryClientViewDTO dto = new CategoryClientViewDTO();
            dto.setCategoryRoomId(Long.valueOf(category.getCategoryRoomId()));
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

                // Buscar la primera imagen real asociada
                if (room.getRoomImages() != null && !room.getRoomImages().isEmpty()) {
                    int imgId = room.getRoomImages().get(0).getImgId();

                    Optional<Img> imageOpt = imgRepository.findById(imgId);
                    if (imageOpt.isPresent()) {
                        Img img = imageOpt.get();
                        dto.setImageUrl("http://localhost:8080/" + img.getPath()); // Ej: uploads/xyz.jpg
                    } else {
                        dto.setImageUrl("/img/default.jpg"); // Si img_id no existe en tabla img
                    }
                } else {
                    dto.setImageUrl("/img/default.jpg"); // Si no hay imágenes asociadas
                }
            });

            return dto;
        }).toList();
    }



}
