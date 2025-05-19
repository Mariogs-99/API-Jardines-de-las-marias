package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoomWithImageDTO {
    private String nameEs;
    private String nameEn;
    private int maxCapacity;
    private String descriptionEs;
    private String descriptionEn;
    private double price;
    private String sizeBed;
    private int categoryRoomId;
    private int quantity;
    private MultipartFile image;

}
