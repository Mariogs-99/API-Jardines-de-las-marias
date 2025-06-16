package com.hotelJB.hotelJB_API.models.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceResponse {

    private String title;
    private String description;
    private String availableDays;

    private Long experienceId;

    private String titleEs;
    private String titleEn;

    private String descriptionEs;
    private String descriptionEn;

    private String duration;
    private Integer capacity;
    private Double price;

    private String availableDaysEs;
    private String availableDaysEn;

    private String imageUrl;
    private Boolean active;
}
