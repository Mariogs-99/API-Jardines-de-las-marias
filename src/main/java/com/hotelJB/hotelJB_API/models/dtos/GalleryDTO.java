package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryDTO {
    private String title;
    private String description;
    private Integer position;
    private Boolean active;
}
