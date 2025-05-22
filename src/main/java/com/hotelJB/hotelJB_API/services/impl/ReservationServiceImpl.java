package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.Room;
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

            room.setQuantity(room.getQuantity() - data.getQuantityReserved());
            roomRepository.save(room);

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

                room.setQuantity(room.getQuantity() - diferencia);
            } else if (diferencia < 0) {
                room.setQuantity(room.getQuantity() + Math.abs(diferencia));
            }

            roomRepository.save(room);

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

            Room room = reservation.getRoom();
            room.setQuantity(room.getQuantity() + reservation.getQuantityReserved());
            roomRepository.save(room);

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

    // Extra: Devuelve lista de ReservationResponse para evitar problemas con fechas en el frontend
    public List<ReservationResponse> getAllResponses() {
        return reservationRepository.findAll().stream()
                .map(res -> new ReservationResponse(
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
                        new RoomResponse(
                                res.getRoom().getRoomId(),
                                res.getRoom().getNameEs(),
                                res.getRoom().getMaxCapacity(),
                                res.getRoom().getDescriptionEs(),
                                res.getRoom().getPrice(),
                                res.getRoom().getSizeBed(),
                                res.getRoom().getQuantity(),
                                res.getRoom().getImg() != null ? res.getRoom().getImg().getPath() : null,
                                null // puedes mapear la categoryRoom si ya tienes CategoryRoomResponse
                        )
                ))
                .collect(Collectors.toList());
    }
}