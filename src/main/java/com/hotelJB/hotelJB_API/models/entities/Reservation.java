package com.hotelJB.hotelJB_API.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "init_date")
    private LocalDate initDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "cant_people")
    private int cantPeople;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "payment")
    private double payment;

    @Column(name = "quantity_reserved")
    private int quantityReserved;

    @Column(name = "room_number")
    private String roomNumber; // ✅ Campo agregado correctamente

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    public Reservation(LocalDate initDate, LocalDate finishDate, int cantPeople, String name, String email,
                       String phone, double payment, Room room, int quantityReserved) {
        this.initDate = initDate;
        this.finishDate = finishDate;
        this.cantPeople = cantPeople;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.payment = payment;
        this.room = room;
        this.quantityReserved = quantityReserved;
    }
}
