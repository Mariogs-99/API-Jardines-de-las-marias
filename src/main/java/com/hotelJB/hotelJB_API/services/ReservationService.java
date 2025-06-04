package com.hotelJB.hotelJB_API.services;


import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReservationService {
    void save(ReservationDTO data) throws Exception;
    void update(ReservationDTO data, int reservationId)  throws Exception;
    void delete(int reservationId)  throws Exception;
    List<Reservation> getAll();
    Optional<Reservation> findById(int reservationId);
    List<Map<String, LocalDate>> getFullyBookedDatesForHotel();

    List<ReservationResponse> getAllResponses();

    List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int cantPeople);
}
