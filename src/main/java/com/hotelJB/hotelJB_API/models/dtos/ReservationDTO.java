package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationDTO {
    private String tempReference;
    private String reservationCode;
    private LocalDate initDate;
    private LocalDate finishDate;
    private int cantPeople;
    private String name;
    private String email;
    private String phone;
    private float payment;
    private String roomNumber; // opcional
    private String status; // ACTIVA, FUTURA o FINALIZADA
    private List<ReservationRoomDTO> rooms;
    private String paypalOrderId;
}
