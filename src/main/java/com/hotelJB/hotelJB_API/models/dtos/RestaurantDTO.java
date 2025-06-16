package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDTO {
    // Español
    private String name;
    private String description;
    private String schedule;

    // Inglés
    private String nameEn;
    private String descriptionEn;
    private String scheduleEn;

    private String pdfMenuUrl;
    private String imgUrl;
    private boolean highlighted;
}
