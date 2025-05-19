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

    //Nueva información útil para mostrar
    private int quantity;      // total de habitaciones de este tipo
    private String imageUrl;   // ruta de la imagen asociada (ej: uploads/imagen.jpg)
}
