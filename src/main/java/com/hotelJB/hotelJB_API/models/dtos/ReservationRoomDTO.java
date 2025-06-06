package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Data;

@Data
public class ReservationRoomDTO {
    private Integer roomId;
    private Integer quantity;
    private String assignedRoomNumber; // opcional
}
