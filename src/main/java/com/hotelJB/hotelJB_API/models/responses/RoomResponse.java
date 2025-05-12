package com.hotelJB.hotelJB_API.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private int roomId;
    private String name;
    private int maxCapacity;
    private String description;
    private double price;
    private String sizeBed;
    private int categoryRoomId;
}
