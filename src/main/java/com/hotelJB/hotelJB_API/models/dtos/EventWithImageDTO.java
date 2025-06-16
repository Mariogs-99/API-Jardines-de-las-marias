package com.hotelJB.hotelJB_API.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventWithImageDTO {

    private String titleEs;
    private String titleEn;

    private String descriptionEs;
    private String descriptionEn;

    private LocalDate eventDate;
    private Integer capacity;
    private BigDecimal price;

    private MultipartFile image;

    private boolean active;
}
