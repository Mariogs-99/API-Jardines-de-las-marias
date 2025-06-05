package com.hotelJB.hotelJB_API.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private int reservationId;
    private LocalDate initDate;
    private LocalDate finishDate;
    private int cantPeople;
    private String name;
    private String email;
    private String phone;
    private double payment;
    private int quantityReserved;
    private LocalDateTime creationDate;
    private String status;
    private RoomResponse room;
    private String roomNumber;
}
