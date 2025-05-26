package com.hotelJB.hotelJB_API.models.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GalleryResponse {
    private Long galleryId;
    private String title;
    private String description;
    private String imageUrl;
    private Integer position;
    private Boolean active;
    private LocalDateTime createdAt;
}

