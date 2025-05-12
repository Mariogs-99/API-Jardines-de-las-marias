package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="detail_room")
public class DetailRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="detail_room_id")
    private int detailRoomId;

    @Column(name="detail_name_es")
    private String detailNameEs;

    @Column(name="detail_name_en")
    private String detailNameEn;

    @Column(name="icon")
    private String icon;

    public DetailRoom(String detailNameEs, String detailNameEn, String icon) {
        this.detailNameEs = detailNameEs;
        this.detailNameEn = detailNameEn;
        this.icon = icon;
    }
}
