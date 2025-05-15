package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryClientViewDTO {
    private Long categoryRoomId;
    private String nameCategoryEs;
    private String descriptionEs;
    private Integer maxPeople;
    private String bedInfo;
    private String roomSize;
    private Boolean hasTv;
    private Boolean hasAc;
    private Boolean hasPrivateBathroom;

    // Info de una habitación asociada
    private BigDecimal minPrice;
    private String imageUrl; // Puedes mapear esto desde imgId


}
