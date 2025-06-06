package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Integer> {
    List<ReservationRoom> findByReservation_ReservationId(Integer reservationId);

    void deleteByReservation_ReservationId(Integer reservationId);

}
