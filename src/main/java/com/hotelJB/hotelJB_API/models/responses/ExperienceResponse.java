package com.hotelJB.hotelJB_API.models.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceResponse {

    private Long experienceId;
    private String title;
    private String description;
    private String duration;
    private Integer capacity;
    private Double price;
    private String availableDays;
    private String imageUrl;
    private Boolean active;
}
