package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private Long id;

    private String titleEs;
    private String titleEn;

    private String descriptionEs;
    private String descriptionEn;

    private LocalDate eventDate;

    private Integer capacity;

    private BigDecimal price;

    private String imageUrl;

    private boolean active;
}
