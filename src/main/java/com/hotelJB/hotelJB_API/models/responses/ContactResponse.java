package com.hotelJB.hotelJB_API.models.responses;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponse {
    private Long contactId;
    private String telephone;
    private String telephone2;
    private String email;
    private String address;
    private String addressUrl;
    private String facebookUsername;
    private String facebookUrl;
    private String instagramUsername;
    private String tiktok;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


