package com.hotelJB.hotelJB_API.repositories;

import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomxImgRepository extends JpaRepository<RoomxImg,Integer> {
    List<RoomxImg> findByRoomId(Integer roomId);
}
