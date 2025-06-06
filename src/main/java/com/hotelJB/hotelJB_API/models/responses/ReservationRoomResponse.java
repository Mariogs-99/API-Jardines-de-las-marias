package com.hotelJB.hotelJB_API.models.responses;

import lombok.Data;

@Data
public class ReservationRoomResponse {
    private Integer roomId;
    private String roomName;
    private String assignedRoomNumber;
    private Integer quantity;
}
