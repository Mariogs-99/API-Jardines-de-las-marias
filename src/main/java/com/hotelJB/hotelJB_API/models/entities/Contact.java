package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contact_id")
    private int contactId;

    @Column(name="telephone")
    private String telephone;

    @Column(name="telephone2")
    private String telephone2;

    @Column(name="address")
    private String address;

    @Column(name="address_url")
    private String addressUrl;

    @Column(name="email")
    private String email;

    @Column(name="instagram_username")
    private String instagram;

    @Column(name="facebook_username")
    private String facebookUsername;

    @Column(name="facebook_url")
    private String facebookUrl;

    @Column(name="tiktok")
    private String tiktok;

    public Contact(String telephone, String telephone2, String address, String addressUrl, String email,
                   String instagram, String facebookUsername, String facebookUrl, String tiktok) {
        this.telephone = telephone;
        this.telephone2 = telephone2;
        this.address = address;
        this.addressUrl = addressUrl;
        this.email = email;
        this.instagram = instagram;
        this.facebookUsername = facebookUsername;
        this.facebookUrl = facebookUrl;
        this.tiktok = tiktok;
    }
}
