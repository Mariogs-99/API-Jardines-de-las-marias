package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private String telephone;
    private String telephone2;
    private String address;
    private String addressUrl;
    private String email;
    private String instagramUsername;
    private String facebookUsername;
    private String facebookUrl;
    private String tiktok;
}