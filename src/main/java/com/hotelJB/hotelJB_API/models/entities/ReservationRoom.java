package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Integer quantity;

    @Column(name = "assigned_room_number")
    private String assignedRoomNumber;
}
