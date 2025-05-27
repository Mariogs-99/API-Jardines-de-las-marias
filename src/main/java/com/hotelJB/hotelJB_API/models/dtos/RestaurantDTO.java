package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDTO {
    private String name;
    private String description;
    private String schedule;
    private String pdfMenuUrl;
    private String imgUrl;
    private boolean highlighted;
}
