package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.RoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ImgRepository imgRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Autowired
    private ReservationRepository reservationRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void save(RoomDTO data) throws Exception {
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
    }

    @Override
    public void update(RoomDTO data, int roomId) throws Exception {
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
    }

    @Override
    public void delete(int roomId) throws Exception {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

        roomRepository.delete(room);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<RoomResponse> findById(int roomId, String lang) {
        return roomRepository.findById(roomId).map(this::mapToRoomResponse);
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int maxCapacity, String lang) {
        return roomRepository.findAll().stream()
                .map(room -> {
                    int reserved = reservationRepository.countReservedQuantityByRoomAndDates(room, initDate, finishDate);
                    int available = room.getQuantity() - reserved;
                    return mapToRoomResponse(room, available);
                })
                //.filter(...) eliminado para incluir todas las habitaciones
                .collect(Collectors.toList());
    }



    @Override
    public List<RoomResponse> findByLanguage(String language) {
        return roomRepository.findAll().stream()
                .map(this::mapToRoomResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getAllWithCategory() {
        return roomRepository.findAll().stream()
                .map(this::mapToRoomResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse mapToRoomResponse(Room room) {
        return mapToRoomResponse(room, room.getQuantity()); // valor por defecto
    }

    private RoomResponse mapToRoomResponse(Room room, int availableQuantity) {
        CategoryRoom cat = room.getCategoryRoom();

        CategoryRoomResponse categoryRoomResponse = new CategoryRoomResponse(
                cat.getCategoryRoomId(),
                cat.getNameCategoryEs(),
                cat.getDescriptionEs(),
                cat.getRoomSize(),
                cat.getBedInfo(),
                null,
                Boolean.TRUE.equals(cat.getHasTv()),
                Boolean.TRUE.equals(cat.getHasAc()),
                Boolean.TRUE.equals(cat.getHasPrivateBathroom())
        );

        String rawPath = room.getImg().getPath().replaceFirst("^/+", "");
        String imageUrl = room.getImg() != null
                ? baseUrl + "/" + rawPath
                : baseUrl + "/images/default-room.jpg";

        return new RoomResponse(
                room.getRoomId(),
                room.getNameEs(),
                room.getNameEn(),
                room.getMaxCapacity(),
                room.getDescriptionEs(),
                room.getDescriptionEn(),
                room.getPrice(),
                room.getSizeBed(),
                room.getQuantity(),
                imageUrl,
                availableQuantity,
                categoryRoomResponse,
                availableQuantity > 0
        );

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
            room.setImg(img);
            roomRepository.save(room);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar habitación con imagen", e);
        }
    }

    @Override
    public void updateRoomWithImage(Integer roomId, RoomWithImageDTO dto) {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            room.setNameEs(dto.getNameEs());
            room.setNameEn(dto.getNameEn());
            room.setDescriptionEn(dto.getDescriptionEn());
            room.setMaxCapacity(dto.getMaxCapacity());
            room.setDescriptionEs(dto.getDescriptionEs());
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

                room.setImg(img);
            }

            roomRepository.save(room);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar habitación con imagen", e);
        }
    }

    @Override
    public boolean isRoomAvailable(Integer roomId, LocalDate initDate, LocalDate finishDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

        int totalQuantity = room.getQuantity();
        int reserved = reservationRepository.countReservedQuantityByRoomAndDates(room, initDate, finishDate);

        return (totalQuantity - reserved) > 0;
    }


}
