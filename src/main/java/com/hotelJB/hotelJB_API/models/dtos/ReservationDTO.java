package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationDTO {
    private LocalDate initDate;
    private LocalDate finishDate;
    private int cantPeople;
    private String name;
    private String email;
    private String phone;
    private float payment;

    // Lista de habitaciones en lugar de solo un roomId
    private List<ReservationRoomDTO> rooms;

    private String roomNumber; // opcional según tu lógica actual
}
