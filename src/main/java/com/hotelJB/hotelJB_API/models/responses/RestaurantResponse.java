package com.hotelJB.hotelJB_API.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponse {
    private Long restaurantId;
    private String name;
    private String description;
    private String schedule;
    private String pdfMenuUrl;
    private String imgUrl;
    private boolean highlighted;
}
