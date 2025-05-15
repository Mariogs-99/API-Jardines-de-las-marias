package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.CategoryRoom;
import com.hotelJB.hotelJB_API.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Integer> {
    List<Room> findByCategoryRoom(CategoryRoom categoryRoom);
    @Query("SELECT r.room FROM Reservation r WHERE " +
            "((r.initDate <= :finishDate AND r.finishDate >= :initDate))")
    List<Room> findReservedRooms(@Param("initDate") LocalDate initDate, @Param("finishDate") LocalDate finishDate);
    @Query("SELECT r FROM Room r WHERE r NOT IN (" +
            "SELECT res.room FROM Reservation res " +
            "WHERE (res.initDate <= :finishDate AND res.finishDate >= :initDate))")
    List<Room> findAvailableRooms(@Param("initDate") LocalDate initDate, @Param("finishDate") LocalDate finishDate);

    Optional<Room> findFirstByCategoryRoom_CategoryRoomIdOrderByPriceAsc(Long categoryRoomId);



}
