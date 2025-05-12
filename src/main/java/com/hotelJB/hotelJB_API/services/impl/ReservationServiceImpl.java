package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.repositories.CategoryRoomRepository;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.ReservationService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CategoryRoomRepository categoryRoomRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(ReservationDTO data) throws Exception {
        try {
            // Buscar la categoría de habitación
            CategoryRoom categoryRoom = categoryRoomRepository.findById(data.getCategoryRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Category Room"));

            // Obtener todas las habitaciones disponibles en esa categoría
            List<Room> availableRooms = roomRepository.findByCategoryRoom(categoryRoom);

            if (availableRooms.isEmpty()) {
                throw new CustomException(ErrorType.NOT_AVAILABLE, "No hay habitaciones disponibles en esta categoría.");
            }

            // Verificar disponibilidad por fecha
            Room selectedRoom = null;
            for (Room room : availableRooms) {
                long count = reservationRepository.countOverlappingReservations(room, data.getInitDate(), data.getFinishDate());
                if (count == 0) {
                    selectedRoom = room;
                    break;
                }
            }

            if (selectedRoom == null) {
                throw new CustomException(ErrorType.NOT_AVAILABLE, "Todas las habitaciones de esta categoría están ocupadas en este período.");
            }

            // Crear y guardar la reserva
            Reservation reservation = new Reservation(
                    data.getInitDate(), data.getFinishDate(), data.getCantPeople(),data.getName(),
                    data.getEmail(), data.getPhone(), data.getPayment(), categoryRoom, selectedRoom
            );

            reservationRepository.save(reservation);

        } catch (CustomException e) {
            throw e;  // Excepción específica, se maneja en el controlador
        } catch (Exception e) {
            throw new Exception("Error al guardar la reserva: " + e.getMessage());
        }
    }


    @Override
    public void update(ReservationDTO data, int reservationId) throws Exception {
        try{
            CategoryRoom categoryRoom = categoryRoomRepository.findById(data.getCategoryRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category Room"));

            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Reservation"));

            reservation.setInitDate(data.getInitDate());
            reservation.setFinishDate(data.getFinishDate());
            reservation.setCantPeople(data.getCantPeople());
            reservation.setName(data.getName());
            reservation.setEmail(data.getEmail());
            reservation.setPhone(data.getPhone());
            reservation.setPayment(data.getPayment());
            reservation.setCategoryroom(categoryRoom);

            reservationRepository.save(reservation);
        }catch (Exception e){
            throw new Exception("Error save Reservation");
        }
    }

    @Override
    public void delete(int reservationId) throws Exception {
        try{
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

            reservationRepository.delete(reservation);
        }catch (Exception e){
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
        // Obtener todas las fechas de las reservas
        List<Object[]> reservedDates = reservationRepository.findAllReservedDates();

        if (reservedDates.isEmpty()) {
            return Collections.emptyList();
        }

        long totalRooms = roomRepository.count();

        // Map para contar cuántas reservas hay por día
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
            if (entry.getValue() >= totalRooms) { // Si el número de reservas en esa fecha >= total de habitaciones
                Map<String, LocalDate> dateMap = new HashMap<>();
                dateMap.put("fullyBookedDate", entry.getKey());
                fullyBookedDates.add(dateMap);
            }
        }

        return fullyBookedDates;
    }

}
