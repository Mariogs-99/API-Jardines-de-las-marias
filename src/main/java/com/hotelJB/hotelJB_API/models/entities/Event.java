package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate eventDate;

    private Integer capacity;

    private BigDecimal price;

    // 🔄 Reemplazamos imageUrl por relación a Img
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "img_id")
    private Img img;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
