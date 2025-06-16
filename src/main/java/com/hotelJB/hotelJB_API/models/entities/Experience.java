package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "experience")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    private String titleEs;
    private String titleEn;

    @Column(columnDefinition = "TEXT")
    private String descriptionEs;

    @Column(columnDefinition = "TEXT")
    private String descriptionEn;

    private String duration;

    private Integer capacity;

    private Double price;

    @Column(name = "available_days_es")
    private String availableDaysEs;

    @Column(name = "available_days_en")
    private String availableDaysEn;

    private String imageUrl;

    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
