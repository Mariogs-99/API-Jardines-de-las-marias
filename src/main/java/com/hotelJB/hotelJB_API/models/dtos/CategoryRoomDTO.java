package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRoomDTO {

    private String nameCategoryEs;
    private String nameCategoryEn;
    private String descriptionEs;
    private String descriptionEn;

    // Nuevos campos
    private Integer maxPeople;
    private String bedInfo;
    private String roomSize;
    private Boolean hasTv;
    private Boolean hasAc;
    private Boolean hasPrivateBathroom;
}
