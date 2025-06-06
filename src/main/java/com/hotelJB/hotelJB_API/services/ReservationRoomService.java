package com.hotelJB.hotelJB_API.services;


import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;

import java.util.List;

public interface ReservationRoomService {
    void saveRoomsForReservation(Integer reservationId, List<ReservationRoomDTO> rooms);

    void deleteByReservationId(Integer reservationId);

}
