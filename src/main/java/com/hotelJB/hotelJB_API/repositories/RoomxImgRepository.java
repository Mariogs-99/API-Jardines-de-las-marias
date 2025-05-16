package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomxImgRepository extends JpaRepository<RoomxImg,Integer> {
    List<RoomxImg> findByRoomId(Integer roomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoomxImg r WHERE r.roomId = :roomId")
    void deleteByRoomId(@Param("roomId") int roomId);

}
