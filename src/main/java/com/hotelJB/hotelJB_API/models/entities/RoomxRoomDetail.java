package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="roomxdetail_room")
public class RoomxRoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_detail_id")
    private int roomDetailId;

    @Column(name="room_id")
    private int roomId;

    @Column(name="detail_room_id")
    private int detailRoomId;

    public RoomxRoomDetail(int roomId, int detailRoomId) {
        this.roomId = roomId;
        this.detailRoomId = detailRoomId;
    }
}
