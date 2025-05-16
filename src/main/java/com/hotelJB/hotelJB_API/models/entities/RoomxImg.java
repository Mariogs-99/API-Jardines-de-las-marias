package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="roomximg")
public class RoomxImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_img_id")
    private int roomImgId;

    @Column(name="room_id")
    private int roomId;

    @Column(name="img_id")
    private int imgId;

    public RoomxImg(int roomId, int imgId) {
        this.roomId = roomId;
        this.imgId = imgId;
    }
}
