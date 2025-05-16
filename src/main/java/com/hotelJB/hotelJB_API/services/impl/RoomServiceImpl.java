package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomDTO;
import com.hotelJB.hotelJB_API.models.dtos.RoomWithImageDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.repositories.RoomxImgRepository;
import com.hotelJB.hotelJB_API.services.RoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private RoomxImgRepository roomxImgRepository;



    @Override
    public void save(RoomDTO data) throws Exception {
        try{
            CategoryRoom categoryRoom = categoryRoomRepository.findById(data.getCategoryRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category Room"));

            Room room = new Room(data.getNameEs(),data.getNameEn(),data.getMaxCapacity(),data.getDescriptionEs(),
                    data.getDescriptionEn(),data.getPrice(),data.getSizeBed(),categoryRoom);
            roomRepository.save(room);
        }catch (Exception e){
            throw new Exception("Error save Room");
        }
    }

    @Override
    public void update(RoomDTO data, int roomId) throws Exception {
        try{
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

            roomRepository.save(room);
        }catch (Exception e){
            throw new Exception("Error update room");
        }
    }

    @Override
    public void delete(int roomId) throws Exception {
        try{
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

            roomRepository.delete(room);
        }catch (Exception e){
            throw new Exception("Error delete room");
        }
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<RoomResponse> findById(int roomId,String lang) {
        Optional<Room> room = roomRepository.findById(roomId);

        return room.map(value -> new RoomResponse(
                value.getRoomId(),
                "es".equals(lang) ? value.getNameEs() : value.getNameEn(),
                value.getMaxCapacity(),
                "es".equals(lang) ? value.getDescriptionEs() : value.getDescriptionEn(),
                value.getPrice(),
                value.getSizeBed(),
                value.getCategoryRoom().getCategoryRoomId()
        ));

    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int maxCapacity, String lang) {
        // Obtener todas las habitaciones
        List<Room> allRooms = roomRepository.findAll();

        // Obtener habitaciones ocupadas en el rango de fechas
        List<Room> reservedRooms = roomRepository.findReservedRooms(initDate, finishDate);

        // Filtrar habitaciones disponibles
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> !reservedRooms.contains(room))
                .filter(room -> room.getMaxCapacity() >= maxCapacity)
                .toList();

        return availableRooms.stream().map(room -> new RoomResponse(
                room.getRoomId(),
                "es".equals(lang) ? room.getNameEs() : room.getNameEn(),
                room.getMaxCapacity(),
                "es".equals(lang) ? room.getDescriptionEs() : room.getDescriptionEn(),
                room.getPrice(),
                room.getSizeBed(),
                room.getCategoryRoom().getCategoryRoomId()
        )).collect(Collectors.toList());
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
                value.getCategoryRoom().getCategoryRoomId()
        )).collect(Collectors.toList());
    }

    @Override
    public void saveRoomWithImage(RoomWithImageDTO dto) {
        try {
            // 1. Crear carpeta si no existe
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 2. Crear nombre de archivo
            String originalFilename = dto.getImage().getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFilename;

            // Ruta absoluta para guardar el archivo
            String absolutePath = uploadDir + fileName;

            // Ruta relativa que se guarda en la base de datos
            String relativePath = "uploads/" + fileName;

            // 3. Guardar imagen en el disco
            File file = new File(absolutePath);
            dto.getImage().transferTo(file);

            // 4. Guardar imagen en la tabla img
            Img img = new Img(fileName, relativePath); // ✅ guardar ruta relativa
            imgRepository.save(img);

            // 5. Buscar categoría
            CategoryRoom categoryRoom = categoryRoomRepository.findById(dto.getCategoryRoomId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            // 6. Guardar habitación
            Room room = new Room(
                    dto.getNameEs(),
                    dto.getNameEn(),
                    dto.getMaxCapacity(),
                    dto.getDescriptionEs(),
                    dto.getDescriptionEn(),
                    dto.getPrice(),
                    dto.getSizeBed(),
                    categoryRoom
            );
            roomRepository.save(room);

            // 7. Guardar relación en roomximg
            RoomxImg relation = new RoomxImg(room.getRoomId(), img.getImgId());
            roomxImgRepository.save(relation);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar habitación con imagen", e);
        }
    }

    @Override
    public void updateRoomWithImage(Integer roomId, RoomWithImageDTO dto) {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            // Actualizar campos
            room.setNameEs(dto.getNameEs());
            room.setNameEn(dto.getNameEn());
            room.setMaxCapacity(dto.getMaxCapacity());
            room.setDescriptionEs(dto.getDescriptionEs());
            room.setDescriptionEn(dto.getDescriptionEn());
            room.setPrice(dto.getPrice());
            room.setSizeBed(dto.getSizeBed());

            CategoryRoom category = categoryRoomRepository.findById(dto.getCategoryRoomId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            room.setCategoryRoom(category);

            // Si subió nueva imagen
            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                // Guardar imagen
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = dto.getImage().getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                String absolutePath = uploadDir + fileName;
                String relativePath = "uploads/" + fileName;

                File file = new File(absolutePath);
                dto.getImage().transferTo(file);

                // Guardar imagen en tabla
                Img img = new Img(fileName, relativePath);
                imgRepository.save(img);

                // Crear nueva relación (sin eliminar las anteriores, opcional)
                // Eliminar relaciones anteriores de roomximg
                roomxImgRepository.deleteByRoomId(roomId);

                    // Crear nueva relación con la nueva imagen
                RoomxImg relation = new RoomxImg(roomId, img.getImgId());
                roomxImgRepository.save(relation);

            }

            roomRepository.save(room);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar habitación con imagen", e);
        }
    }







}
