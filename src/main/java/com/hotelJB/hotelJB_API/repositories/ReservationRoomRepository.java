package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Integer> {
    List<ReservationRoom> findByReservation_ReservationId(Integer reservationId);

    void deleteByReservation_ReservationId(Integer reservationId);

    @Query("""
    SELECT COALESCE(SUM(rr.quantity), 0)
    FROM ReservationRoom rr
    JOIN rr.reservation r
    WHERE rr.room.roomId = :roomId
      AND r.status != 'FINALIZADA'
      AND r.initDate <= :finishDate
      AND r.finishDate >= :initDate
""")
    int countReservedQuantityForRoomInRange(
            @Param("roomId") int roomId,
            @Param("initDate") java.time.LocalDate initDate,
            @Param("finishDate") java.time.LocalDate finishDate
    );


    @Query("""
SELECT COUNT(rr) > 0 FROM ReservationRoom rr
JOIN rr.reservation res
WHERE rr.assignedRoomNumber = :assignedRoomNumber
AND res.status IN ('ACTIVA', 'FUTURA')
AND res.reservationId != :reservationId
""")
    boolean existsByAssignedRoomNumberAndOtherReservation(String assignedRoomNumber, int reservationId);


}
