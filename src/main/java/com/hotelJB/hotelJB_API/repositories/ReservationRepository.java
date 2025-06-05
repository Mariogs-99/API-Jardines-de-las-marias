package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
        SELECT COALESCE(SUM(r.quantityReserved), 0) FROM Reservation r
        WHERE r.room = :room
        AND (r.initDate < :finishDate AND r.finishDate > :initDate)
    """)
    int countReservedQuantityByRoomAndDates(
            @Param("room") Room room,
            @Param("initDate") LocalDate initDate,
            @Param("finishDate") LocalDate finishDate
    );

    @Query("SELECT r.initDate, r.finishDate FROM Reservation r")
    List<Object[]> findAllReservedDates();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.room.roomId = :roomId AND " +
            "r.initDate < :finishDate AND r.finishDate > :initDate")
    int countOverlappingReservations(@Param("roomId") Integer roomId,
                                     @Param("initDate") LocalDate initDate,
                                     @Param("finishDate") LocalDate finishDate);

    // 🔄 NUEVO: reserva activa según fechas
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.roomNumber = :roomNumber
        AND r.initDate <= CURRENT_DATE
        AND r.finishDate >= CURRENT_DATE
    """)
    Optional<Reservation> findActiveByRoomNumber(@Param("roomNumber") String roomNumber);

    Optional<Reservation> findTopByRoomNumber(String roomNumber);
}
