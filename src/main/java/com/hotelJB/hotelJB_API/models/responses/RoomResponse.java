package com.hotelJB.hotelJB_API.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private int roomId;
    private String nameEs;
    private String nameEn;
    private int maxCapacity;
    private String descriptionEs;
    private String descriptionEn;
    private double price;
    private String sizeBed;
    private int quantity;
    private String imageUrl;
    private int availableQuantity;
    private CategoryRoomResponse categoryRoom;
    private boolean available;
}
