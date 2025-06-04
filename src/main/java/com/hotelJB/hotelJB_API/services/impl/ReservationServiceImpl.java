package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.ReservationService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void save(ReservationDTO data) throws Exception {
        try {
            Room room = roomRepository.findById(data.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

            int totalReserved = reservationRepository.countReservedQuantityByRoomAndDates(
                    room, data.getInitDate(), data.getFinishDate());

            int available = room.getQuantity() - totalReserved;

            if (available < data.getQuantityReserved()) {
                throw new CustomException(ErrorType.NOT_AVAILABLE, "No hay suficientes habitaciones disponibles.");
            }

            Reservation reservation = new Reservation(
                    data.getInitDate(),
                    data.getFinishDate(),
                    data.getCantPeople(),
                    data.getName(),
                    data.getEmail(),
                    data.getPhone(),
                    data.getPayment(),
                    room,
                    data.getQuantityReserved()
            );

            reservationRepository.save(reservation);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error al guardar la reserva: " + e.getMessage());
        }
    }

    @Override
    public void update(ReservationDTO data, int reservationId) throws Exception {
        try {
            Room room = roomRepository.findById(data.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));

            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

            int cantidadAnterior = reservation.getQuantityReserved();
            int cantidadNueva = data.getQuantityReserved();
            int diferencia = cantidadNueva - cantidadAnterior;

            if (diferencia > 0) {
                int totalReserved = reservationRepository.countReservedQuantityByRoomAndDates(
                        room, data.getInitDate(), data.getFinishDate());

                int available = room.getQuantity() - totalReserved;

                if (available < diferencia) {
                    throw new CustomException(ErrorType.NOT_AVAILABLE,
                            "No hay suficientes habitaciones disponibles para modificar esta reserva.");
                }
            }

            reservation.setInitDate(data.getInitDate());
            reservation.setFinishDate(data.getFinishDate());
            reservation.setCantPeople(data.getCantPeople());
            reservation.setName(data.getName());
            reservation.setEmail(data.getEmail());
            reservation.setPhone(data.getPhone());
            reservation.setPayment(data.getPayment());
            reservation.setRoom(room);
            reservation.setQuantityReserved(cantidadNueva);

            reservationRepository.save(reservation);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error al actualizar la reserva: " + e.getMessage());
        }
    }

    @Override
    public void delete(int reservationId) throws Exception {
        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

            reservationRepository.delete(reservation);
        } catch (Exception e) {
            throw new Exception("Error delete reservation");
        }
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(int reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public List<Map<String, LocalDate>> getFullyBookedDatesForHotel() {
        List<Object[]> reservedDates = reservationRepository.findAllReservedDates();
        if (reservedDates.isEmpty()) return Collections.emptyList();

        long totalRooms = roomRepository.count();
        Map<LocalDate, Integer> reservationCountByDate = new HashMap<>();

        for (Object[] dates : reservedDates) {
            LocalDate startDate = (LocalDate) dates[0];
            LocalDate endDate = (LocalDate) dates[1];
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                reservationCountByDate.put(current, reservationCountByDate.getOrDefault(current, 0) + 1);
                current = current.plusDays(1);
            }
        }

        List<Map<String, LocalDate>> fullyBookedDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : reservationCountByDate.entrySet()) {
            if (entry.getValue() >= totalRooms) {
                Map<String, LocalDate> dateMap = new HashMap<>();
                dateMap.put("fullyBookedDate", entry.getKey());
                fullyBookedDates.add(dateMap);
            }
        }

        return fullyBookedDates;
    }

    @Override
    public List<ReservationResponse> getAllResponses() {
        return reservationRepository.findAll().stream()
                .map(res -> {
                    Room room = res.getRoom();
                    CategoryRoomResponse categoryRoomResponse = null;

                    if (room.getCategoryRoom() != null) {
                        var cat = room.getCategoryRoom();
                        categoryRoomResponse = new CategoryRoomResponse(
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
                    }

                    String imageUrl = room.getImg() != null ? room.getImg().getPath() : null;

                    RoomResponse roomResponse = new RoomResponse(
                            room.getRoomId(),
                            room.getNameEs(),
                            room.getMaxCapacity(),
                            room.getDescriptionEs(),
                            room.getPrice(),
                            room.getSizeBed(),
                            room.getQuantity(),
                            imageUrl,
                            room.getQuantity(), // disponible actual asumida
                            categoryRoomResponse
                    );

                    // 📆 Lógica de estado de la reserva
                    String status;
                    LocalDate today = LocalDate.now();
                    if (res.getFinishDate().isBefore(today)) {
                        status = "FINALIZADA";
                    } else if (res.getInitDate().isAfter(today)) {
                        status = "FUTURA";
                    } else {
                        status = "ACTIVA";
                    }

                    return new ReservationResponse(
                            res.getReservationId(),
                            res.getInitDate(),
                            res.getFinishDate(),
                            res.getCantPeople(),
                            res.getName(),
                            res.getEmail(),
                            res.getPhone(),
                            res.getPayment(),
                            res.getQuantityReserved(),
                            res.getCreationDate(),
                            status,
                            roomResponse
                    );

                }).collect(Collectors.toList());
    }


    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int cantPeople) {
        List<Room> allRooms = roomRepository.findAll();
        List<RoomResponse> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (room.getMaxCapacity() < cantPeople) continue;

            int reserved = reservationRepository.countReservedQuantityByRoomAndDates(room, initDate, finishDate);
            int disponibles = room.getQuantity() - reserved;

            CategoryRoomResponse categoryResponse = null;
            if (room.getCategoryRoom() != null) {
                var cat = room.getCategoryRoom();
                categoryResponse = new CategoryRoomResponse(
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
            }

            availableRooms.add(new RoomResponse(
                    room.getRoomId(),
                    room.getNameEs(),
                    room.getMaxCapacity(),
                    room.getDescriptionEs(),
                    room.getPrice(),
                    room.getSizeBed(),
                    room.getQuantity(),
                    room.getImg() != null ? room.getImg().getPath() : null,
                    disponibles,
                    categoryResponse
            ));
        }

        return availableRooms;
    }
}
