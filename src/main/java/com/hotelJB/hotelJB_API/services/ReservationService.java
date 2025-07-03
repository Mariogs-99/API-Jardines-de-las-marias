package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReservationService {
    // Crear una nueva reserva
    ReservationResponse save(ReservationDTO data) throws Exception;
    // Actualizar una reserva existente
    void update(ReservationDTO data, int reservationId) throws Exception;

    // Eliminar una reserva por ID
    void delete(int reservationId) throws Exception;

    // Obtener todas las reservas sin mapear
    List<Reservation> getAll();

    // Buscar una reserva por ID
    Optional<Reservation> findById(int reservationId);

    // Obtener las fechas en las que el hotel está totalmente reservado
    List<Map<String, LocalDate>> getFullyBookedDatesForHotel();

    // Obtener todas las reservas con respuesta formateada
    List<ReservationResponse> getAllResponses();

    // Obtener habitaciones disponibles en un rango de fechas y para una cantidad de personas
    List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int cantPeople);

    // Asignar número de habitación a una reserva
    void assignRoomNumber(int reservationId, String roomNumber) throws Exception;

    // Obtener la reserva activa por número de habitación
    Reservation getActiveReservationByRoomNumber(String roomNumber) throws Exception;

    // Obtener los detalles de una reserva por número de habitación
    ReservationResponse getByRoomNumber(String roomNumber) throws Exception;

    // Verificar si un número de habitación está actualmente en uso en una reserva activa
    boolean isRoomNumberInUse(String roomNumber);


    void assignRoomNumbers(int reservationId, List<ReservationRoomDTO> assignments) throws Exception;

    void saveEntity(Reservation reservation);

    //?Buscar por medio de reserva
    ReservationResponse getByReservationCode(String reservationCode) throws Exception;

    ReservationResponse saveWithStatus(ReservationDTO data, String status) throws Exception;

    String buildReservationEmailBody(Reservation reservation);
}
