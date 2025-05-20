package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.RoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CategoryRoomRepository categoryRoomRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Autowired
    private ImgRepository imgRepository;

    @Override
    public void save(RoomDTO data) throws Exception {
        try {
            CategoryRoom categoryRoom = categoryRoomRepository.findById(data.getCategoryRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category Room"));

            Room room = new Room(
                    data.getNameEs(),
                    data.getNameEn(),
                    data.getMaxCapacity(),
                    data.getDescriptionEs(),
                    data.getDescriptionEn(),
                    data.getPrice(),
                    data.getSizeBed(),
                    categoryRoom,
                    data.getQuantity()
            );

            roomRepository.save(room);
        } catch (Exception e) {
            throw new Exception("Error save Room");
        }
    }

    @Override
    public void update(RoomDTO data, int roomId) throws Exception {
        try {
            CategoryRoom categoryRoom = categoryRoomRepository.findById(data.getCategoryRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category Room"));

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

            room.setNameEs(data.getNameEs());
            room.setNameEn(data.getNameEn());
            room.setMaxCapacity(data.getMaxCapacity());
            room.setDescriptionEs(data.getDescriptionEs());
            room.setDescriptionEn(data.getDescriptionEn());
            room.setPrice(data.getPrice());
            room.setSizeBed(data.getSizeBed());
            room.setQuantity(data.getQuantity());
            room.setCategoryRoom(categoryRoom);

            roomRepository.save(room);
        } catch (Exception e) {
            throw new Exception("Error update room");
        }
    }

    @Override
    public void delete(int roomId) throws Exception {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

            roomRepository.delete(room);
        } catch (Exception e) {
            throw new Exception("Error delete room");
        }
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<RoomResponse> findById(int roomId, String lang) {
        Optional<Room> room = roomRepository.findById(roomId);

        return room.map(value -> new RoomResponse(
                value.getRoomId(),
                "es".equals(lang) ? value.getNameEs() : value.getNameEn(),
                value.getMaxCapacity(),
                "es".equals(lang) ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getPrice(),
                value.getSizeBed(),
                value.getCategoryRoom().getCategoryRoomId(),
                value.getQuantity(),
                value.getImg() != null ? value.getImg().getPath() : null
        ));
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int maxCapacity, String lang) {
        List<Room> availableRooms = roomRepository.findRoomsWithAvailableQuantity(initDate, finishDate);

        return availableRooms.stream()
                .filter(room -> room.getMaxCapacity() >= maxCapacity)
                .map(room -> new RoomResponse(
                        room.getRoomId(),
                        "es".equals(lang) ? room.getNameEs() : room.getNameEn(),
                        room.getMaxCapacity(),
                        "es".equals(lang) ? room.getDescriptionEs() : room.getDescriptionEn(),
                        room.getPrice(),
                        room.getSizeBed(),
                        room.getCategoryRoom().getCategoryRoomId(),
                        room.getQuantity(),
                        room.getImg() != null ? room.getImg().getPath() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> findByLanguage(String language) {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream().map(value -> new RoomResponse(
                value.getRoomId(),
                "es".equals(language) ? value.getNameEs() : value.getNameEn(),
                value.getMaxCapacity(),
                "es".equals(language) ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getPrice(),
                value.getSizeBed(),
                value.getCategoryRoom().getCategoryRoomId(),
                value.getQuantity(),
                value.getImg() != null ? value.getImg().getPath() : null
        )).collect(Collectors.toList());
    }

    @Override
    public void saveRoomWithImage(RoomWithImageDTO dto) {
        try {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = dto.getImage().getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String absolutePath = uploadDir + fileName;
            String relativePath = "uploads/" + fileName;

            File file = new File(absolutePath);
            dto.getImage().transferTo(file);

            Img img = new Img(fileName, relativePath);
            imgRepository.save(img);

            CategoryRoom categoryRoom = categoryRoomRepository.findById(dto.getCategoryRoomId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Room room = new Room(
                    dto.getNameEs(),
                    dto.getNameEn(),
                    dto.getMaxCapacity(),
                    dto.getDescriptionEs(),
                    dto.getDescriptionEn(),
                    dto.getPrice(),
                    dto.getSizeBed(),
                    categoryRoom,
                    dto.getQuantity()
            );
            room.setImg(img); // ✅ relación directa
            roomRepository.save(room);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar habitación con imagen", e);
        }
    }

    @Override
    public List<RoomResponse> getAllWithCategory() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomResponse(
                        room.getRoomId(),
                        room.getNameEs(), // puedes usar idioma si deseas
                        room.getMaxCapacity(),
                        room.getDescriptionEs(),
                        room.getPrice(),
                        room.getSizeBed(),
                        room.getCategoryRoom() != null ? room.getCategoryRoom().getCategoryRoomId() : null,
                        room.getQuantity(),
                        room.getImg() != null ? room.getImg().getPath() : null
                ))
                .collect(Collectors.toList());
    }


    @Override
    public void updateRoomWithImage(Integer roomId, RoomWithImageDTO dto) {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            room.setNameEs(dto.getNameEs());
            room.setNameEn(dto.getNameEn());
            room.setMaxCapacity(dto.getMaxCapacity());
            room.setDescriptionEs(dto.getDescriptionEs());
            room.setDescriptionEn(dto.getDescriptionEn());
            room.setPrice(dto.getPrice());
            room.setSizeBed(dto.getSizeBed());
            room.setQuantity(dto.getQuantity());

            CategoryRoom category = categoryRoomRepository.findById(dto.getCategoryRoomId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            room.setCategoryRoom(category);

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = dto.getImage().getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                String absolutePath = uploadDir + fileName;
                String relativePath = "uploads/" + fileName;

                File file = new File(absolutePath);
                dto.getImage().transferTo(file);

                Img img = new Img(fileName, relativePath);
                imgRepository.save(img);

                room.setImg(img); // ✅ actualizar imagen
            }

            roomRepository.save(room);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar habitación con imagen", e);
        }
    }
}
