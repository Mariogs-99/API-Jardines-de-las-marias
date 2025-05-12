package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String titleEs;
    private String titleEn;
    private String descriptionEs;
    private String descriptionEn;
    private String pathImage;
    private int categoryId;
}
