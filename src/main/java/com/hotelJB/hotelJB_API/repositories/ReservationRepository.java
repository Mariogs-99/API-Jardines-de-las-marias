package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // ✅ Nueva: total de habitaciones reservadas en rango de fechas
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

    // 🔁 Opcional: esto puedes eliminarlo si ya no lo usas
    @Query("SELECT r.initDate, r.finishDate FROM Reservation r")
    List<Object[]> findAllReservedDates();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.room.roomId = :roomId AND " +
            "r.initDate < :finishDate AND r.finishDate > :initDate")
    int countOverlappingReservations(@Param("roomId") Integer roomId,
                                     @Param("initDate") LocalDate initDate,
                                     @Param("finishDate") LocalDate finishDate);

}
