package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Data // Incluye getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    private String telephone;
    private String telephone2;
    private String email;
    private String address;

    @Column(name = "address_url")
    private String addressUrl;

    @Column(name = "facebook_username")
    private String facebookUsername;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "instagram_username")
    private String instagramUsername;

    @Column(name = "tiktok_url")
    private String tiktok;


    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void setUpdateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
