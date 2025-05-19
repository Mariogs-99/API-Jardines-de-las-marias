package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String nameEs;
    private String nameEn;
    private int maxCapacity;
    private String descriptionEs;
    private String descriptionEn;
    private double price;
    private String sizeBed;
    private int categoryRoomId;
    private int quantity;

    //Ruta de imagen para el frontend
    private String imageUrl;
}
