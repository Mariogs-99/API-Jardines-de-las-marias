package com.hotelJB.hotelJB_API.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private int reservationId;
    private String reservationCode;
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
    private List<ReservationRoomResponse> rooms;
    private String roomNumber;
    private String dteControlNumber;
    private String wompiCheckoutUrl;
}
