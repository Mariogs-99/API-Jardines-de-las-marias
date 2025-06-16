package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    // Español
    private String name;
    private String description;
    private String schedule;

    // Inglés
    private String nameEn;
    private String descriptionEn;
    private String scheduleEn;

    private String pdfMenuUrl;
    private String imgUrl;
    private boolean highlighted;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void setUpdateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
