package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.ReservationRoom;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.ReservationRoomRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.ReservationRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationRoomServiceImpl implements ReservationRoomService {

    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    public void saveRoomsForReservation(Integer reservationId, List<ReservationRoomDTO> rooms) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        for (ReservationRoomDTO dto : rooms) {
            Room room = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            ReservationRoom rr = ReservationRoom.builder()
                    .reservation(reservation)
                    .room(room)
                    .quantity(dto.getQuantity())
                    .assignedRoomNumber(dto.getAssignedRoomNumber())
                    .build();

            reservationRoomRepository.save(rr);
        }
    }

    @Override
    public void deleteByReservationId(Integer reservationId) {
        reservationRoomRepository.deleteByReservation_ReservationId(reservationId);
    }

}
