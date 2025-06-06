package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.ReservationRoom;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.ReservationRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.ReservationRoomRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.ReservationRoomService;
import com.hotelJB.hotelJB_API.services.ReservationService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRoomRepository reservationRoomRepository;

    @Autowired
    private ReservationRoomService reservationRoomService;

    @Override
    public void save(ReservationDTO data) throws Exception {
        Reservation reservation = new Reservation(
                data.getInitDate(),
                data.getFinishDate(),
                data.getCantPeople(),
                data.getName(),
                data.getEmail(),
                data.getPhone(),
                data.getPayment(),
                null,
                0
        );

        reservation.setRoomNumber(data.getRoomNumber());
        reservationRepository.save(reservation);

        reservationRoomService.saveRoomsForReservation(reservation.getReservationId(), data.getRooms());
    }

    @Override
    public void update(ReservationDTO data, int reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        reservation.setInitDate(data.getInitDate());
        reservation.setFinishDate(data.getFinishDate());
        reservation.setCantPeople(data.getCantPeople());
        reservation.setName(data.getName());
        reservation.setEmail(data.getEmail());
        reservation.setPhone(data.getPhone());
        reservation.setPayment(data.getPayment());
        reservation.setRoomNumber(data.getRoomNumber());

        reservationRepository.save(reservation);

        reservationRoomService.deleteByReservationId(reservationId);
        reservationRoomService.saveRoomsForReservation(reservationId, data.getRooms());
    }

    @Override
    public void delete(int reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));
        reservationRepository.delete(reservation);
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
                    List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(res.getReservationId());

                    List<ReservationRoomResponse> roomResponses = reservationRooms.stream().map(rr -> {
                        Room r = rr.getRoom();
                        ReservationRoomResponse resp = new ReservationRoomResponse();
                        resp.setRoomId(r.getRoomId());
                        resp.setRoomName(r.getNameEs());
                        resp.setAssignedRoomNumber(rr.getAssignedRoomNumber());
                        resp.setQuantity(rr.getQuantity());
                        return resp;
                    }).collect(Collectors.toList());

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
                            roomResponses,
                            res.getRoomNumber()
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
                    disponibles,
                    room.getImg() != null ? room.getImg().getPath() : null,
                    disponibles,
                    categoryResponse
            ));
        }

        return availableRooms;
    }

    @Override
    public void assignRoomNumber(int reservationId, String roomNumber) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        boolean roomInUse = reservationRepository.findActiveByRoomNumber(roomNumber).isPresent();
        if (roomInUse) {
            throw new CustomException(ErrorType.NOT_AVAILABLE, "Esa habitación ya está asignada a otra reserva activa.");
        }

        reservation.setRoomNumber(roomNumber);
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getActiveReservationByRoomNumber(String roomNumber) throws Exception {
        return reservationRepository.findActiveByRoomNumber(roomNumber)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reserva activa no encontrada."));
    }

    @Override
    public boolean isRoomNumberInUse(String roomNumber) {
        return reservationRepository.findActiveByRoomNumber(roomNumber).isPresent();
    }

    @Override
    public ReservationResponse getByRoomNumber(String roomNumber) throws Exception {
        Reservation reservation = reservationRepository.findTopByRoomNumber(roomNumber)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(reservation.getReservationId());

        List<ReservationRoomResponse> roomResponses = reservationRooms.stream().map(rr -> {
            Room r = rr.getRoom();
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(rr.getAssignedRoomNumber());
            resp.setQuantity(rr.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        String status;
        LocalDate today = LocalDate.now();
        if (reservation.getFinishDate().isBefore(today)) {
            status = "FINALIZADA";
        } else if (reservation.getInitDate().isAfter(today)) {
            status = "FUTURA";
        } else {
            status = "ACTIVA";
        }

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                status,
                roomResponses,
                reservation.getRoomNumber()
        );
    }
}
