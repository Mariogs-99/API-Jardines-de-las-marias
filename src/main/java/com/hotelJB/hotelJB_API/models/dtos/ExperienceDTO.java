package com.hotelJB.hotelJB_API.models.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceDTO {

    private String title;
    private String description;
    private String duration;
    private Integer capacity;
    private Double price;
    private String availableDays;
    private String imageUrl;
    private Boolean active;
}
