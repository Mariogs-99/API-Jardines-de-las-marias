package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id")
    private int reservationId;

    @Column(name="init_date")
    private LocalDate initDate;

    @Column(name="finish_date")
    private LocalDate finishDate;

    @Column(name="cant_people")
    private int cantPeople;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="payment")
    private double payment;

    @Column(name="creation_date", updatable = false, insertable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_room_id")
    private CategoryRoom categoryroom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="room_id")
    private Room room;

    public Reservation(LocalDate initDate, LocalDate finishDate, int cantPeople, String name, String email,
                       String phone, double payment, CategoryRoom categoryroom, Room room) {
        this.initDate = initDate;
        this.finishDate = finishDate;
        this.cantPeople = cantPeople;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.payment = payment;
        this.categoryroom = categoryroom;
        this.room = room;
    }
}
