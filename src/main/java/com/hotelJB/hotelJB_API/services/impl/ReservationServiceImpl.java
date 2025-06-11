package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;
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
import jakarta.transaction.Transactional;
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

    @Autowired
    private ReservationRoomRepository reservationRoomRepository;

    @Autowired
    private ReservationRoomService reservationRoomService;

    @Override
    public ReservationResponse save(ReservationDTO data) throws Exception {
        int totalReserved = data.getRooms().stream()
                .mapToInt(ReservationRoomDTO::getQuantity)
                .sum();

        Reservation reservation = new Reservation(
                data.getInitDate(),
                data.getFinishDate(),
                data.getCantPeople(),
                data.getName(),
                data.getEmail(),
                data.getPhone(),
                data.getPayment(),
                null,
                totalReserved
        );

        String initialStatus = data.getInitDate().isAfter(LocalDate.now()) ? "FUTURA" : "ACTIVA";
        reservation.setStatus(initialStatus);
        reservation.setRoomNumber(data.getRoomNumber());

        if (!data.getRooms().isEmpty()) {
            int firstRoomId = data.getRooms().get(0).getRoomId();
            Room room = roomRepository.findById(firstRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            reservation.setRoom(room);
        }

        reservationRepository.save(reservation);
        reservationRoomService.saveRoomsForReservation(reservation.getReservationId(), data.getRooms());

        List<ReservationRoomResponse> roomResponses = data.getRooms().stream().map(roomDTO -> {
            Room r = roomRepository.findById(roomDTO.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(roomDTO.getAssignedRoomNumber());
            resp.setQuantity(roomDTO.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber()
        );
    }

    @Override
    @Transactional
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
        reservation.setStatus(data.getStatus());

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

                    return new ReservationResponse(
                            res.getReservationId(),
                            res.getReservationCode(),
                            res.getInitDate(),
                            res.getFinishDate(),
                            res.getCantPeople(),
                            res.getName(),
                            res.getEmail(),
                            res.getPhone(),
                            res.getPayment(),
                            res.getQuantityReserved(),
                            res.getCreationDate(),
                            res.getStatus(),
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
            int reserved = reservationRoomRepository.countReservedQuantityForRoomInRange(
                    room.getRoomId(), initDate, finishDate
            );

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
                    categoryResponse,
                    disponibles > 0
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

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber()
        );
    }

    @Override
    public void assignRoomNumbers(int reservationId, List<ReservationRoomDTO> assignments) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(reservationId);

        for (ReservationRoomDTO dto : assignments) {
            for (ReservationRoom rr : reservationRooms) {
                if (rr.getRoom().getRoomId() == dto.getRoomId()) {
                    rr.setAssignedRoomNumber(dto.getAssignedRoomNumber());
                    rr.setQuantity(dto.getQuantity());
                }
            }
        }

        reservationRoomRepository.saveAll(reservationRooms);
    }

    @Override
    public void saveEntity(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
